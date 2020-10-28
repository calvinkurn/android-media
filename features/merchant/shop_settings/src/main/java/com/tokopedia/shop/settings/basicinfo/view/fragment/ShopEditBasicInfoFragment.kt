package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.analytics.ShopSettingsTracking
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChangesData
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_MESSAGE
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_SHOP_BASIC_DATA_MODEL
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.REQUEST_EDIT_BASIC_INFO
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.ShopSettingsErrorHandler
import com.tokopedia.shop.settings.common.util.ShopTypeDef
import com.tokopedia.shop.settings.common.util.setNavigationResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_edit_basic_info.*
import javax.inject.Inject

class ShopEditBasicInfoFragment: Fragment() {

    companion object {
        private const val SAVED_IMAGE_PATH = "saved_img_path"
        private const val MAX_FILE_SIZE_IN_KB = 10240
        private const val REQUEST_CODE_IMAGE = 846
        private const val MIN_INPUT_LENGTH = 3
    }

    @Inject
    lateinit var viewModel: ShopEditBasicInfoViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopDomainTextWatcher: TextWatcher? = null
    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var snackbar: Snackbar? = null
    private var tvSave: TextView? = null
    private var savedLocalImageUrl: String? = null
    private var needUpdatePhotoUI: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initGqlClient()
        super.onCreate(savedInstanceState)
        setupToolbar()

        savedLocalImageUrl = savedInstanceState?.getString(SAVED_IMAGE_PATH).orEmpty()
        arguments?.let {
            val cacheManagerId = ShopEditBasicInfoFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
            shopBasicDataModel = saveInstanceCacheManager.get(EXTRA_SHOP_BASIC_DATA_MODEL, ShopBasicDataModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_edit_basic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextField()
        setupDomainSuggestion()
        setupShopAvatar()
        setupSaveBtn()

        observeLiveData()
        getAllowShopNameDomainChanges()
        container.requestFocus()

        showShopInformation(shopBasicDataModel)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_IMAGE_PATH, savedLocalImageUrl)
    }

    override fun onResume() {
        super.onResume()
        if (needUpdatePhotoUI) {
            updatePhotoUI(shopBasicDataModel)
            needUpdatePhotoUI = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    override fun onPause() {
        super.onPause()
        dismissToaster()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                savedLocalImageUrl = imageUrlOrPathList[0]
            }
            needUpdatePhotoUI = true
            determineSubmitButton()
        }
    }

    private fun dismissToaster() {
        snackbar?.dismiss()
    }

    private fun initGqlClient() {
        context?.let { GraphqlClient.init(it) }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar? = activity?.findViewById(R.id.toolbar)
        toolbar?.title = getString(R.string.shop_settings_basic_info)

        tvSave = activity?.findViewById(R.id.tvSave)
        tvSave?.show()
    }

    private fun setupTextField() {
        setShopDomainTextWatcher()
        setupShopNameTextField()
        setupShopDomainTextField()
        setupShopTagLineTextField()
        setupShopDescriptionTextField()
    }

    private fun setupShopTagLineTextField() {
        shopTagLineTextField.textFieldInput.isSingleLine = false
        shopTagLineTextField.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                shopTagLineTextField.setMessage("")
                shopTagLineTextField.setError(false)
                determineSubmitButton()
            }
        })
    }

    private fun setupShopDescriptionTextField() {
        shopDescriptionTextField.textFieldInput.isSingleLine = false
        shopDescriptionTextField.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                shopDescriptionTextField.setMessage("")
                shopDescriptionTextField.setError(false)
                determineSubmitButton()
            }
        })
    }

    private fun setupDomainSuggestion() {
        shopDomainSuggestions.setOnItemClickListener { domain ->
            shopDomainTextField.textFieldInput.apply {
                removeTextChangedListener(shopDomainTextWatcher)
                resetShopDomainInput()
                setText(domain)
                setSelection(text.length)
                addTextChangedListener(shopDomainTextWatcher)
            }
            shopDomainSuggestions.hide()
        }
    }

    private fun setupShopAvatar() {
        imageAvatar.setOnClickListener { openImagePicker() }
        textChangeAvatar.setOnClickListener {
            openImagePicker()
            ShopSettingsTracking.clickChangeShopLogo(userSession.shopId, getShopType())
        }
    }

    private fun setupSaveBtn() {
        tvSave?.setOnClickListener {
            val isDialogShown = !isNameStillSame() || !isDomainStillSame()
            if (isDialogShown) {
                createSaveDialog()
            } else {
                onSaveButtonClicked()
            }
        }
    }

    private fun setupShopNameTextField() {
        shopNameTextField.textFieldInput.apply {
            val textWatcher = createShopNameTextWatcher()
            setText(shopBasicDataModel?.name)
            addTextChangedListener(textWatcher)
            isEnabled = false
            isSingleLine = false
        }
    }

    private fun setupShopDomainTextField() {
        shopDomainTextField.textFieldInput.apply {
            setText(shopBasicDataModel?.domain)
            addTextChangedListener(shopDomainTextWatcher)
            isEnabled = false
            isSingleLine = false
        }
    }

    private fun createShopNameTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopDomainSuggestions.hide()
            }

            override fun afterTextChanged(s: Editable) {
                if (!isNameStillSame()) {
                    val input = s.toString()
                    if (input.isBlank()) {
                        val message = context?.getString(R.string.error_validation_shop_name_empty).orEmpty()
                        showShopNameInputError(message)
                        viewModel.cancelValidateShopName()
                    } else {
                        resetShopNameInput()
                        viewModel.validateShopName(input)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        }
    }

    private fun setShopDomainTextWatcher() {
        shopDomainTextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopDomainSuggestions.hide()
            }

            override fun afterTextChanged(s: Editable) {
                if (!isDomainStillSame()) {
                    val input = s.toString()
                    if (input.isBlank()) {
                        val message = context?.getString(R.string.error_validation_shop_domain_empty).orEmpty()
                        showShopDomainInputError(message)
                        viewModel.cancelValidateShopDomain()
                    } else {
                        resetShopDomainInput()
                        viewModel.validateShopDomain(input)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        }
    }

    private fun showShopNameInputError(message: String) {
        shopNameTextField.setError(true)
        shopNameTextField.setMessage(message)
        determineSubmitButton()
    }

    private fun showShopDomainInputError(message: String) {
        shopDomainTextField.setError(true)
        shopDomainTextField.setMessage(message)
        determineSubmitButton()
    }

    private fun resetShopNameInput() {
        shopNameTextField.setError(false)
        shopNameTextField.setMessage("")
        determineSubmitButton()
    }

    private fun resetShopDomainInput() {
        shopDomainTextField.setError(false)
        shopDomainTextField.setMessage("")
        determineSubmitButton()
    }

    private fun disableSaveBtn() {
        tvSave?.isEnabled = false
        tvSave?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
    }

    private fun enableSaveBtn() {
        tvSave?.isEnabled = true
        tvSave?.setTextColor(ContextCompat.getColor(requireContext(), R.color.merchant_green))
    }

    private fun isShopNameTextFieldError(): Boolean {
        return shopNameTextField.isTextFieldError
    }

    private fun isShopDomainTextFieldError(): Boolean {
        return shopDomainTextField.isTextFieldError
    }

    private fun isSavedLocalImageUrlEmpty(): Boolean {
        return savedLocalImageUrl.isNullOrEmpty()
    }

    private fun isNameStillSame(): Boolean {
        return shopBasicDataModel?.name == shopNameTextField.textFieldInput.text.toString()
    }

    private fun isDomainStillSame(): Boolean {
        return shopBasicDataModel?.domain == shopDomainTextField.textFieldInput.text.toString()
    }

    private fun isTagLineStillSame(): Boolean {
        return shopBasicDataModel?.tagline == shopTagLineTextField.textFieldInput.text.toString()
    }

    private fun isDescriptionStillSame(): Boolean {
        return shopBasicDataModel?.description == shopDescriptionTextField.textFieldInput.text.toString()
    }

    private fun isEverythingStillSame(): Boolean {
        return isNameStillSame() && isDomainStillSame() && isTagLineStillSame() && isDescriptionStillSame() && isSavedLocalImageUrlEmpty()
    }

    private fun determineSubmitButton() {
        if (isEverythingStillSame() || isShopDomainTextFieldError() || isShopNameTextFieldError()) {
            disableSaveBtn()
        } else {
            enableSaveBtn()
        }
    }

    private fun observeLiveData() {
        observeGetShopBasicData()
        observeUploadShopImage()
        observeUpdateShopData()
        observeAllowShopNameDomainChanges()
        observeValidateShopNameDomain()
        observeShopDomainSuggestions()
    }

    private fun getAllowShopNameDomainChanges() {
        viewModel.getAllowShopNameDomainChanges()
    }

    private fun observeGetShopBasicData() {
        observe(viewModel.shopBasicData) {
            when(it) {
                is Success -> showShopInformation(it.data)
                is Fail -> onErrorGetShopBasicData(it.throwable)
            }
        }
    }

    private fun observeUploadShopImage() {
        observe(viewModel.uploadShopImage) {
            when(it) {
                is Fail -> {
                    it.throwable.cause?.apply {
                        onErrorUploadShopImage(this)
                    }
                }
            }
        }
    }

    private fun observeUpdateShopData() {
        observe(viewModel.updateShopBasicData) {
            when(it) {
                is Success -> {
                    it.data.graphQLSuccessMessage?.let { graphQlSuccesMessage ->
                        graphQlSuccesMessage.message?.let { message ->
                            if (graphQlSuccesMessage.isSuccess) {
                                onSuccessUpdateShopBasicData(message)
                            } else {
                                onErrorUpdateShopBasicData(message)
                            }
                        }
                    }
                }
                is Fail -> onErrorUpdateShopBasicData(it.throwable)
            }
        }
    }

    private fun observeAllowShopNameDomainChanges() {
        observe(viewModel.allowShopNameDomainChanges) {
            when(it) {
                is Success -> {
                    val data = it.data
                    showShopEditShopInfoTicker(data)
                    showShopNameDomainTextField(data)
                }
                is Fail -> {
                    val throwable = it.throwable
                    showAllowShopNameDomainChangesError(throwable)
                }
            }
            progressBar.hide()
            container.show()
        }
    }

    private fun observeValidateShopNameDomain() {
        observe(viewModel.validateShopName) {
            when(it) {
                is Success -> {
                    val validateDomainShopName = it.data.validateDomainShopName
                    val notValid = !validateDomainShopName.isValid

                    if(notValid) {
                        val message = validateDomainShopName.error.message
                        showShopNameInputError(message)
                    }
                }
                is Fail -> {
                    val message = context?.getString(R.string.error_validation_shop_name_domain).orEmpty()
                    showShopNameInputError(message)
                    shopDomainSuggestions.hide()
                    ShopSettingsErrorHandler.logMessage(it.throwable.message ?: "")
                    ShopSettingsErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }

        observe(viewModel.validateShopDomain) {
            when(it) {
                is Success -> {
                    val validateDomainShopName = it.data.validateDomainShopName
                    val notValid = !validateDomainShopName.isValid

                    if(notValid) {
                        val message = validateDomainShopName.error.message
                        showShopDomainInputError(message)
                    }
                }
                is Fail -> {
                    val message = context?.getString(R.string.error_validation_shop_name_domain).orEmpty()
                    showShopDomainInputError(message)
                    shopDomainSuggestions.hide()
                    ShopSettingsErrorHandler.logMessage(it.throwable.message ?: "")
                    ShopSettingsErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeShopDomainSuggestions() {
        observe(viewModel.shopDomainSuggestion) {
            when(it) {
                is Success -> {
                    val result = it.data.shopDomainSuggestion.result
                    val shopDomains = result.shopDomains
                    shopDomainSuggestions.show(shopDomains)
                }
            }
        }
    }

    private fun showShopEditShopInfoTicker(data: AllowShopNameDomainChangesData) {
        if (GlobalConfig.isSellerApp()) {
            val isNameAllowed = data.isNameAllowed
            val isDomainAllowed = data.isDomainAllowed
            val reasonNameNotAllowed = data.reasonNameNotAllowed
            val reasonDomainNotAllowed = data.reasonDomainNotAllowed

            if (reasonNameNotAllowed.isBlank() && reasonDomainNotAllowed.isBlank()) {
                return
            }
            when {
                isNameAllowed && isDomainAllowed -> showTicker(reasonNameNotAllowed, Ticker.TYPE_WARNING)
                isNameAllowed && !isDomainAllowed -> showTicker(reasonDomainNotAllowed, Ticker.TYPE_INFORMATION)
                isDomainAllowed && !isNameAllowed -> showTicker(reasonNameNotAllowed, Ticker.TYPE_INFORMATION)
                else -> showTicker(reasonNameNotAllowed, Ticker.TYPE_INFORMATION)
            }
        } else {
            showTicker(getString(R.string.shop_edit_ticker_wording_for_customer_app), Ticker.TYPE_INFORMATION)
        }
    }

    private fun showShopNameDomainTextField(data: AllowShopNameDomainChangesData) {
        val isNameAllowed = data.isNameAllowed
        val isDomainAllowed = data.isDomainAllowed

        val shopNameInput = shopNameTextField.textFieldInput
        val shopDomainInput = shopDomainTextField.textFieldInput

        if (GlobalConfig.isSellerApp()) {
            shopNameInput.isEnabled = isNameAllowed
            shopDomainInput.isEnabled = isDomainAllowed
        } else {
            shopNameInput.isEnabled = false
            shopDomainInput.isEnabled = false
        }
    }

    private fun showTicker(message: String, type: Int) {
        shopEditTicker.tickerType = type
        shopEditTicker.setHtmlDescription(message)
        shopEditTicker.setDescriptionClickEvent(object : TickerCallback{
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (type == Ticker.TYPE_WARNING) {
                    clickReadMore(linkUrl)
                }
            }
            override fun onDismiss() {}
        })
        shopEditTicker.show()
    }

    private fun initInjector() {
        (activity?.application as? BaseMainApplication)?.baseAppComponent?.let { baseAppComponent ->
            DaggerShopSettingsComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun onSaveButtonClicked() {
        showSubmitLoading()

        val name = shopNameTextField.textFieldInput.text.toString()
        val domain = shopDomainTextField.textFieldInput.text.toString()
        val tagLine = shopTagLineTextField.textFieldInput.text.toString()
        val desc = shopDescriptionTextField.textFieldInput.text.toString()

        if (!isSavedLocalImageUrlEmpty()) {
            viewModel.uploadShopImage(savedLocalImageUrl ?: "", name, domain, tagLine, desc)
        } else {
            viewModel.updateShopBasicData(name, domain, tagLine, desc)
        }

        tvSave?.isEnabled = false
    }

    private fun showSubmitLoading() {
        progressBar.show()
        container.hide()
    }

    private fun hideSubmitLoading() {
        progressBar.hide()
        container.show()
    }

    private fun loadShopBasicData() {
        viewModel.getShopBasicData()
    }

    private fun openImagePicker() {
        val builder = ImagePickerBuilder(getString(R.string.choose_shop_picture),
            intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA), GalleryType.IMAGE_ONLY, MAX_FILE_SIZE_IN_KB,
            ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
            ImagePickerEditorBuilder(
                intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                false, null), null)
        val intent = ImagePickerActivity.getIntent(context, builder)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }


    private fun onSuccessUpdateShopBasicData(successMessage: String) {
        hideSubmitLoading()

        val bundle = Bundle().apply {
            putString(EXTRA_MESSAGE, successMessage)
        }
        setNavigationResult(bundle, REQUEST_EDIT_BASIC_INFO)
        findNavController().navigateUp()
    }

    private fun onErrorUpdateShopBasicData(throwable: Throwable) {
        hideSubmitLoading()
        showSnackBarErrorSubmitEdit(throwable)
        tvSave?.isEnabled = true
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun onErrorUpdateShopBasicData(message: String) {
        hideSubmitLoading()
        showSnackBarErrorSubmitEdit(message)
        tvSave?.isEnabled = true
        ShopSettingsErrorHandler.logMessage(message)
        ShopSettingsErrorHandler.logExceptionToCrashlytics(message)
    }

    private fun showShopInformation(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            this.shopBasicDataModel = it.apply {
                name = MethodChecker.fromHtml(name).toString()
                domain = MethodChecker.fromHtml(domain).toString()
                description = MethodChecker.fromHtml(description).toString()
                tagline = MethodChecker.fromHtml(tagline).toString()
            }
            this.shopBasicDataModel?.let { model ->
                setUIShopBasicData(model)
                setShopBasicData(model)
            }
            tvSave?.visible()
        }
    }

    private fun setShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        viewModel.setCurrentShopData(shopBasicDataModel)
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        updatePhotoUI(shopBasicDataModel)

        shopTagLineTextField.textFieldInput.run {
            if (TextUtils.isEmpty(text)) {
                setText(shopBasicDataModel.tagline)
                text?.length?.let { setSelection(it) }
            }
        }

        shopDescriptionTextField.textFieldInput.run {
            if (TextUtils.isEmpty(text)) {
                setText(shopBasicDataModel.description)
                text?.length?.let { setSelection(it) }
            }
        }
    }

    private fun updatePhotoUI(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            if (TextUtils.isEmpty(savedLocalImageUrl)) {
                val logoUrl = it.logo
                if (TextUtils.isEmpty(logoUrl)) {
                    imageAvatar.setImageDrawable(
                        MethodChecker.getDrawable(imageAvatar.context, R.drawable.ic_shop_edit_avatar))
                } else {
                    ImageHandler.LoadImage(imageAvatar, logoUrl)
                }
            } else {
                ImageHandler.LoadImage(imageAvatar, savedLocalImageUrl)
            }
        }
    }

    private fun onErrorGetShopBasicData(throwable: Throwable) {
        showSnackBarErrorShopInfo(throwable)
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun showSnackBarErrorShopInfo(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        snackbar = Toaster.build(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            loadShopBasicData()
        })
        snackbar?.show()
    }

    private fun onErrorUploadShopImage(throwable: Throwable) {
        showSnackBarErrorSubmitEdit(throwable)
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun showSnackBarErrorSubmitEdit(throwable: Throwable) {
        val message = ShopSettingsErrorHandler.getErrorMessage(context, throwable)
        message?.apply {
            snackbar = Toaster.build(container, this, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                onSaveButtonClicked()
            })
            snackbar?.show()
        }
    }

    private fun showSnackBarErrorSubmitEdit(message: String) {
        snackbar = Toaster.build(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            onSaveButtonClicked()
        })
        snackbar?.show()
    }

    private fun showAllowShopNameDomainChangesError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        snackbar = Toaster.build(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            viewModel.getAllowShopNameDomainChanges()
        })
        snackbar?.show()
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun clickReadMore(linkUrl: CharSequence) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, linkUrl.toString())
        ShopSettingsTracking.clickRedirectToPusatSeller(userSession.shopId, getShopType())
    }

    private fun getShopType(): String {
        return when {
            shopBasicDataModel?.isOfficialStore ?: false -> ShopTypeDef.OFFICIAL_STORE
            shopBasicDataModel?.isGold ?: false -> ShopTypeDef.GOLD_MERCHANT
            else -> ShopTypeDef.REGULAR_MERCHANT
        }
    }

    private fun createSaveDialog() {
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.shop_edit_dialog_title))
            setDescription(getString(R.string.shop_edit_dialog_description))
            setPrimaryCTAText(getString(R.string.shop_edit_dialog_primary_cta))
            setSecondaryCTAText(getString(R.string.shop_edit_dialog_secondary_cta))
            setPrimaryCTAClickListener {
                onSaveButtonClicked()
                dismiss()
                ShopSettingsTracking.clickConfirmChangeShopName(userSession.shopId, getShopType())
            }
            setSecondaryCTAClickListener {
                dismiss()
                ShopSettingsTracking.clickCancelChangeShopName(userSession.shopId, getShopType())
            }
        }.show()
    }
}