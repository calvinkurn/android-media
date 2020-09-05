package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
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
import com.tokopedia.shop.settings.analytics.ShopSettingsTracking
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity.Companion.EXTRA_MESSAGE
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity.Companion.EXTRA_SHOP_MODEL
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopEditBasicInfoAdapter
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopEditBasicInfoAdapterFactoryImpl
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditMainInfoModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditProfilePictureModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditTickerModel
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditMainInfoViewHolder
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditProfilePictureViewHolder
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditTickerViewHolder
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.ShopTypeDef
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_edit_basic_info.*
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import javax.inject.Inject

class ShopEditBasicInfoFragment: Fragment(),
        ShopEditMainInfoViewHolder.ShopEditMainInfoListener,
        ShopEditProfilePictureViewHolder.ShopEditProfilePictureListener,
        ShopEditTickerViewHolder.ShopEditTickerListener
{

    companion object {
        private const val URL_PUSAT_SELLER = "https://seller.tokopedia.com/edu/cara-memilih-nama-toko-online/"
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

    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var savedLocalImageUrl: String? = null
    private var needUpdatePhotoUI: Boolean = false
    private var shopEditBasicInfoAdapter: ShopEditBasicInfoAdapter? = null
    private var listVisitable: ArrayList<Visitable<*>>? = null
    private var shopEditProfilePictureModel = ShopEditProfilePictureModel()
    private var shopEditTickerModel = ShopEditTickerModel()
    private var shopEditMainInfoModel = ShopEditMainInfoModel()

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
        setupSaveBtn()
        setupShopEditAdapter()

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

    override fun onCancelValidateShopName() {
        viewModel.cancelValidateShopName()
    }

    override fun onCancelValidateShopDomain() {
        viewModel.cancelValidateShopDomain()
    }

    override fun onValidateShopName(name: String) {
        viewModel.validateShopName(name)
    }

    override fun onValidateShopDomain(domain: String) {
        viewModel.validateShopDomain(domain)
    }

    override fun onImageChangeAvatarClick() {
        openImagePicker()
    }

    override fun onTextChangeAvatarClick() {
        openImagePicker()
        ShopSettingsTracking.clickChangeShopLogo(userSession.shopId, getShopType())
    }

    override fun onReadMoreClick() {
        clickReadMore()
    }

    private fun initGqlClient() {
        context?.let { GraphqlClient.init(it) }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setTitle(getString(R.string.shop_settings_basic_info_title))
        }
    }

    private fun setupSaveBtn() {
        tvSave.setOnClickListener { createSaveDialog() }
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
                    shopEditMainInfoModel.apply {
                        isNameAllowed = data.isNameAllowed
                        isDomainAllowed = data.isDomainAllowed
                    }
                }
                is Fail -> {
                    val throwable = it.throwable
                    showAllowShopNameDomainChangesError(throwable)
                }
            }
        }
    }

    private fun observeValidateShopNameDomain() {
        observe(viewModel.validateShopName) {
            when(it) {
                is Success -> {
                    val validateDomainShopName = it.data.validateDomainShopName
                    shopEditMainInfoModel.nameNotValid = !validateDomainShopName.isValid

                    if(shopEditMainInfoModel.nameNotValid) {
                        val message = validateDomainShopName.error.message
                        shopEditMainInfoModel.nameErrorMessage = message
                    }
                }
                is Fail -> {
                    val message = (it as Success).data.validateDomainShopName.error.message
                    shopEditMainInfoModel.nameErrorMessage = message
                }
            }
        }

        observe(viewModel.validateShopDomain) {
            when(it) {
                is Success -> {
                    val validateDomainShopName = it.data.validateDomainShopName
                    shopEditMainInfoModel.domainNotValid = !validateDomainShopName.isValid

                    if(shopEditMainInfoModel.domainNotValid) {
                        val message = validateDomainShopName.error.message
                        shopEditMainInfoModel.domainErrorMessage = message
                    }
                }
                is Fail -> {
                    val message = (it as Success).data.validateDomainShopName.error.message
                    shopEditMainInfoModel.domainErrorMessage = message
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
                    shopEditMainInfoModel.shopDomains = shopDomains
                }
            }
        }
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

        val name = shopEditMainInfoModel.name
        val domain = shopEditMainInfoModel.domain
        val tagLine = shopEditMainInfoModel.tagLine
        val desc = shopEditMainInfoModel.description

        if (!savedLocalImageUrl.isNullOrEmpty()) {
            viewModel.uploadShopImage(savedLocalImageUrl!!, name, domain, tagLine, desc)
        } else {
            viewModel.updateShopBasicData(name, domain, tagLine, desc)
        }

        tvSave.isEnabled = false
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
        tvSave.isEnabled = true
    }

    private fun showShopInformation(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            this.shopBasicDataModel = it
            setUIShopBasicData(it)
            setShopBasicData(it)
            tvSave.visible()
        }
    }

    private fun setShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        viewModel.setCurrentShopData(shopBasicDataModel)
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        updatePhotoUI(shopBasicDataModel)

        shopBasicDataModel.name?.let {
            shopEditMainInfoModel.name = it
        }
        shopBasicDataModel.domain?.let {
            shopEditMainInfoModel.domain = it
        }
        shopBasicDataModel.tagline?.let {
            shopEditMainInfoModel.tagLine = it
        }
        shopBasicDataModel.description?.let {
            shopEditMainInfoModel.description = it
        }
    }

    private fun updatePhotoUI(shopBasicDataModel: ShopBasicDataModel?) {
        shopBasicDataModel?.let {
            shopEditProfilePictureModel.savedLocalImageUrl = savedLocalImageUrl ?: ""
            shopEditProfilePictureModel.logoUrl = it.logo ?: ""
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

    private fun clickReadMore() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_PUSAT_SELLER)
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
                ShopSettingsTracking.clickConfirmChangeShopName(userSession.shopId, getShopType())
            }
            setSecondaryCTAClickListener {
                dismiss()
                ShopSettingsTracking.clickCancelChangeShopName(userSession.shopId, getShopType())
            }
        }.show()
    }

    private fun setupShopEditAdapter() {
        shopEditBasicInfoAdapter = ShopEditBasicInfoAdapter(ShopEditBasicInfoAdapterFactoryImpl(this,this,this))
        listVisitable = ArrayList()
        listVisitable?.add(ShopEditProfilePictureViewHolder.POSITION, shopEditProfilePictureModel)
        listVisitable?.add(ShopEditTickerViewHolder.POSITION, shopEditTickerModel)
        listVisitable?.add(ShopEditMainInfoViewHolder.POSITION, shopEditMainInfoModel)
        shopEditBasicInfoAdapter?.setVisitables(listVisitable)
    }
}