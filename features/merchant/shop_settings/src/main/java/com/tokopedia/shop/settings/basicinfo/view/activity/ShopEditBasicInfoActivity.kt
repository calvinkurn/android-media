@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.basicinfo.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.*
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopSettingsInfoPresenter
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import kotlinx.android.synthetic.main.activity_shop_edit_basic_info.*
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class ShopEditBasicInfoActivity : BaseSimpleActivity(), UpdateShopSettingsInfoPresenter.View {

    @Inject
    lateinit var updateShopSettingsInfoPresenter: UpdateShopSettingsInfoPresenter

    lateinit var shopBasicDataModel: ShopBasicDataModel
    private var progressDialog: ProgressDialog? = null

    private var savedLocalImageUrl: String? = null
    private var needUpdatePhotoUI: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(this)
        if (savedInstanceState != null) {
            savedLocalImageUrl = savedInstanceState.getString(SAVED_IMAGE_PATH)
        }
        shopBasicDataModel = intent.getParcelableExtra(EXTRA_SHOP_MODEL)
        super.onCreate(savedInstanceState)

        DaggerShopSettingsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        updateShopSettingsInfoPresenter.attachView(this)

        parentTvBrowseFile.setBackground(MethodChecker
                .getDrawable(parentTvBrowseFile.getContext(), R.drawable.ic_balloon_gray))

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
        val OnBrowseClickListener = View.OnClickListener { openImagePicker() }
        ivLogo.setOnClickListener(OnBrowseClickListener)
        tvBrowseFile.setOnClickListener(OnBrowseClickListener)

        tvSave.setOnClickListener { onSaveButtonClicked() }
        vgRoot.requestFocus()

        onSuccessGetShopBasicData(shopBasicDataModel)
    }

    private fun onSaveButtonClicked() {
        showSubmitLoading(getString(R.string.title_loading))
        val tagLine = etShopSlogan.text.toString()
        val desc = etShopDesc.text.toString()
        if (!savedLocalImageUrl.isNullOrEmpty()) {
            updateShopSettingsInfoPresenter.uploadShopImage(savedLocalImageUrl!!, tagLine, desc)
        } else {
            updateShopSettingsInfoPresenter.updateShopBasicData(tagLine, desc)
        }
    }

    fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog!!.setMessage(message)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun hideSubmitLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    private fun loadShopBasicData() {
        updateShopSettingsInfoPresenter.getShopBasicData()
    }

    public override fun onResume() {
        super.onResume()
        if (needUpdatePhotoUI) {
            updatePhotoUI(shopBasicDataModel)
            needUpdatePhotoUI = false
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        updateShopSettingsInfoPresenter.detachView()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                savedLocalImageUrl = imageUrlOrPathList[0]
            }
            needUpdatePhotoUI = true
        }
    }

    private fun openImagePicker() {
        val builder = ImagePickerBuilder(getString(R.string.choose_shop_picture),
                intArrayOf(TYPE_GALLERY, TYPE_CAMERA), GalleryType.IMAGE_ONLY, MAX_FILE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                ImagePickerEditorBuilder(
                        intArrayOf(ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE),
                        false, null), null)
        val intent = ImagePickerActivity.getIntent(this, builder)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }


    override fun onSuccessUpdateShopBasicData(successMessage: String) {
        hideSubmitLoading()
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_MESSAGE, successMessage) })
        finish()
    }

    override fun onErrorUpdateShopBasicData(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    override fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel
        setUIShopBasicData(shopBasicDataModel)
        tvSave.visibility = View.VISIBLE
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
    }

    private fun updatePhotoUI(shopBasicDataModel: ShopBasicDataModel) {
        if (TextUtils.isEmpty(savedLocalImageUrl)) {
            val logoUrl = shopBasicDataModel.logo
            if (TextUtils.isEmpty(logoUrl)) {
                ivLogo.setImageDrawable(
                        MethodChecker.getDrawable(ivLogo.getContext(),R.drawable.ic_camera_add))
            } else {
                ImageHandler.LoadImage(ivLogo, logoUrl)
            }
        } else {
            ImageHandler.LoadImage(ivLogo, savedLocalImageUrl)
        }
    }

    @SuppressLint("Range")
    override fun onErrorGetShopBasicData(throwable: Throwable) {
        showSnackbarErrorShopInfo(throwable)
    }

    private fun showSnackbarErrorShopInfo(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable)
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again)) { loadShopBasicData() }.show()
    }

    override fun onErrorUploadShopImage(throwable: Throwable) {
        showSnackbarErrorSubmitEdit(throwable)
    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable)
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again)) { onSaveButtonClicked() }.show()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_IMAGE_PATH, savedLocalImageUrl)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_edit_basic_info
    }

    companion object {

        val SAVED_IMAGE_PATH = "saved_img_path"
        private val MAX_FILE_SIZE_IN_KB = 10240
        private val REQUEST_CODE_IMAGE = 846

        val EXTRA_SHOP_MODEL = "shop_model"
        val EXTRA_MESSAGE = "message"

        @JvmStatic
        fun createIntent(context: Context, shopBasicDataModel: ShopBasicDataModel?): Intent {
            val intent = Intent(context, ShopEditBasicInfoActivity::class.java)
            intent.putExtra(EXTRA_SHOP_MODEL, shopBasicDataModel)
            return intent
        }
    }


}
