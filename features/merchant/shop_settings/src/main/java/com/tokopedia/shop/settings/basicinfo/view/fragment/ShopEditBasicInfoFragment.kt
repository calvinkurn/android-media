package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges.*
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity.Companion.EXTRA_MESSAGE
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity.Companion.EXTRA_SHOP_MODEL
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_edit_basic_info.*
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import javax.inject.Inject

class ShopEditBasicInfoFragment: Fragment() {

    companion object {
        private const val SAVED_IMAGE_PATH = "saved_img_path"
        private const val MAX_FILE_SIZE_IN_KB = 10240
        private const val REQUEST_CODE_IMAGE = 846

        @JvmStatic
        fun newInstance(bundle: Bundle?): ShopEditBasicInfoFragment {
            return ShopEditBasicInfoFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var viewModel: ShopEditBasicInfoViewModel

    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var savedLocalImageUrl: String? = null
    private var needUpdatePhotoUI: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initGqlClient()
        super.onCreate(savedInstanceState)

        savedLocalImageUrl = savedInstanceState?.getString(SAVED_IMAGE_PATH).orEmpty()
        shopBasicDataModel = arguments?.getParcelable(EXTRA_SHOP_MODEL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_edit_basic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupTextField()
        observeLiveData()

        getAllowShopNameDomainChanges()

        parentTvBrowseFile.background = MethodChecker.getDrawable(parentTvBrowseFile.context,
            com.tokopedia.design.R.drawable.ic_balloon_gray)

        etShopSlogan.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                tilShopSlogan.error = null
            }
        })

        etShopDesc.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                tilShopDesc.error = null
            }
        })

        val onBrowseClickListener = View.OnClickListener { openImagePicker() }
        ivLogo.setOnClickListener(onBrowseClickListener)
        tvBrowseFile.setOnClickListener(onBrowseClickListener)

        tvSave.setOnClickListener { onSaveButtonClicked() }
        container.requestFocus()

        onSuccessGetShopBasicData(shopBasicDataModel)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                savedLocalImageUrl = imageUrlOrPathList[0]
            }
            needUpdatePhotoUI = true
        }
    }

    private fun initGqlClient() {
        context?.let { GraphqlClient.init(it) }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setTitle(R.string.shop_settings_information_edit)
        }
    }

    private fun setupTextField() {
        shopNameTextField.textFieldInput.isEnabled = false
        shopDomainTextField.textFieldInput.isEnabled = false
    }

    private fun observeLiveData() {
        observeGetShopBasicData()
        observeUploadShopImage()
        observeUpdateShopData()
        observeAllowShopNameDomainChanges()
    }

    private fun getAllowShopNameDomainChanges() {
        viewModel.getAllowShopNameDomainChanges()
    }

    private fun observeGetShopBasicData() {
        observe(viewModel.shopBasicData) {
            when(it) {
                is Success -> onSuccessGetShopBasicData(it.data)
                is Fail -> onErrorGetShopBasicData(it.throwable)
            }
        }
    }

    private fun observeUploadShopImage() {
        observe(viewModel.uploadShopImage) {
            when(it) {
                is Fail -> onErrorUploadShopImage(it.throwable)
            }
        }
    }

    private fun observeUpdateShopData() {
        observe(viewModel.updateShopBasicData) {
            when(it) {
                is Success -> onSuccessUpdateShopBasicData(it.data)
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
                    showAllowShopNameDomainChangesError(it.throwable)
                }
            }
        }
    }

    private fun showShopEditShopInfoTicker(data: AllowShopNameDomainChangesData) {
        val isNameAllowed = data.isNameAllowed
        val isDomainAllowed = data.isDomainAllowed

        when {
            isNameAllowed && isDomainAllowed -> showWarningTicker()
            isNameAllowed && !isDomainAllowed -> showDomainNotAllowedTicker(data)
            isDomainAllowed && !isNameAllowed -> showNameNotAllowedTicker(data)
            else -> showNameAndDomainNotAllowedTicker()
        }

        shopEditTicker.show()
    }

    private fun showShopNameDomainTextField(data: AllowShopNameDomainChangesData) {
        val isNameAllowed = data.isNameAllowed
        val isDomainAllowed = data.isDomainAllowed

        val shopNameInput = shopNameTextField.textFieldInput
        val shopDomainInput = shopDomainTextField.textFieldInput

        when {
            isNameAllowed && isDomainAllowed -> {
                shopNameInput.isEnabled = true
                shopDomainInput.isEnabled = true
            }
            isNameAllowed && !isDomainAllowed -> shopDomainInput.isEnabled = true
            isDomainAllowed && !isNameAllowed -> shopNameInput.isEnabled = true
        }
    }

    private fun showWarningTicker() {
        val message = context?.getString(R.string.ticker_warning_can_only_change_shopname_once).orEmpty()
        shopEditTicker.tickerType = Ticker.TYPE_WARNING
        shopEditTicker.setTextDescription(message)
    }

    private fun showDomainNotAllowedTicker(data: AllowShopNameDomainChangesData) {
        val message = data.reasonDomainNotAllowed
        showInfoTicker(message)
    }

    private fun showNameNotAllowedTicker(data: AllowShopNameDomainChangesData) {
        val message = data.reasonNameNotAllowed
        showInfoTicker(message)
    }

    private fun showNameAndDomainNotAllowedTicker() {
        val message = "Kamu sudah pernah mengubah nama dan domain toko sebelumnya."
        showInfoTicker(message)
    }

    private fun showInfoTicker(message: String) {
        shopEditTicker.tickerType = Ticker.TYPE_INFORMATION
        shopEditTicker.setTextDescription(message)
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
        val tagLine = etShopSlogan.text.toString()
        val desc = etShopDesc.text.toString()
        if (!savedLocalImageUrl.isNullOrEmpty()) {
            viewModel.uploadShopImage(savedLocalImageUrl!!, tagLine, desc)
        } else {
            viewModel.updateShopBasicData(tagLine, desc)
        }
    }

    private fun showSubmitLoading() {
        progressBar.show()
        scrollViewContent.hide()
    }

    private fun hideSubmitLoading() {
        progressBar.hide()
        scrollViewContent.show()
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

        val data = Intent().putExtra(EXTRA_MESSAGE, successMessage)
        activity?.setResult(Activity.RESULT_OK, data)
        activity?.finish()
    }

    private fun onErrorUpdateShopBasicData(throwable: Throwable) {
        hideSubmitLoading()
        showSnackBarErrorSubmitEdit(throwable)
    }

    private fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            this.shopBasicDataModel = it
            setUIShopBasicData(it)
            tvSave.visible()
        }
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        updatePhotoUI(shopBasicDataModel)
        //to reserve saveInstanceState from edittext
        if (TextUtils.isEmpty(etShopSlogan.text)) {
            etShopSlogan.setText(shopBasicDataModel.tagline)
            etShopSlogan.text?.length?.let { etShopSlogan.setSelection(it) }
        }
        if (TextUtils.isEmpty(etShopDesc.text)) {
            etShopDesc.setText(shopBasicDataModel.description)
            etShopDesc.text?.length?.let { etShopDesc.setSelection(it) }
        }

        shopNameTextField.textFieldInput.setText(shopBasicDataModel.name)
        shopDomainTextField.textFieldInput.setText(shopBasicDataModel.domain)
    }

    private fun updatePhotoUI(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            if (TextUtils.isEmpty(savedLocalImageUrl)) {
                val logoUrl = it.logo
                if (TextUtils.isEmpty(logoUrl)) {
                    ivLogo.setImageDrawable(
                        MethodChecker.getDrawable(ivLogo.context, com.tokopedia.design.R.drawable.ic_camera_add))
                } else {
                    ImageHandler.LoadImage(ivLogo, logoUrl)
                }
            } else {
                ImageHandler.LoadImage(ivLogo, savedLocalImageUrl)
            }
        }
    }

    private fun onErrorGetShopBasicData(throwable: Throwable) {
        showSnackBarErrorShopInfo(throwable)
    }

    private fun showSnackBarErrorShopInfo(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.make(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            loadShopBasicData()
        })
    }

    private fun onErrorUploadShopImage(throwable: Throwable) {
        showSnackBarErrorSubmitEdit(throwable)
    }

    private fun showSnackBarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.make(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            onSaveButtonClicked()
        })
    }

    private fun showAllowShopNameDomainChangesError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.make(container, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            viewModel.getAllowShopNameDomainChanges()
        })
    }
}