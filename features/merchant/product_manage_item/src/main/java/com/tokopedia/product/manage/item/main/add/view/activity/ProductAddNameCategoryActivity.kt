package com.tokopedia.product.manage.item.main.add.view.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.util.RequestPermissionUtil
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.main.add.view.fragment.ProductAddNameCategoryFragment
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment
import com.tokopedia.product.manage.item.main.base.view.listener.ProductAddImageView
import com.tokopedia.product.manage.item.main.base.view.presenter.ProductAddImagePresenter
import com.tokopedia.product.manage.item.utils.ProductEditItemComponentInstance
import permissions.dispatcher.*

@RuntimePermissions
open class ProductAddNameCategoryActivity : BaseSimpleActivity(), HasComponent<ProductComponent>, ProductAddImageView {
    var tkpdProgressDialog: TkpdProgressDialog? = null
    private var productAddImagePresenter: ProductAddImagePresenter? = null
    private var imageUrls: ArrayList<String>? = null
    private val MAX_IMAGES = 5
    val IMAGE = "image/"
    val IMAGE_OR_VIDEO = "*/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title =""
    }
  
    override fun getComponent() = ProductEditItemComponentInstance.getComponent(application)

    override fun getNewFragment(): Fragment = ProductAddNameCategoryFragment.createInstance(imageUrls)

    override fun getLayoutRes() =  R.layout.activity_product_edit_with_menu

    protected fun createProductAddFragment() {
        if (fragment != null) {
            return
        }
        inflateFragment()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun handleImageUrlFromExternal() {
        showProgressDialog()
        val oriImageUrls = intent.getStringArrayListExtra(BaseProductAddEditFragment.EXTRA_IMAGES)
        val imagesCount = if (oriImageUrls.size > MAX_IMAGES) MAX_IMAGES else oriImageUrls.size
        imageUrls = ArrayList<String>()
        for (i in 0 until imagesCount) {
            val imageUrl = oriImageUrls[i]
            imageUrls!!.add(imageUrl)
        }
        dismissDialog()
        createProductAddFragment()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun handleImageUrlImplicitSingle() {
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        val imageUris = ArrayList<Uri>()
        imageUris.add(imageUri)
        processMultipleImage(imageUris)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun handleImageUrlImplicitMultiple() {
        val imageUris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        if (CommonUtils.checkCollectionNotNull<ArrayList<Uri>>(imageUris)) {
            processMultipleImage(imageUris)
        } else {
            handleImageUrlImplicitSingle()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        createProductAddFragment()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        createProductAddFragment()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForExternalStorage(request: PermissionRequest) {
        request.proceed()
    }

    override fun setupFragment(savedInstance: Bundle?) {
        if (fragment != null) {
            return
        }
        if (checkExplicitImageUrls()) {
            ProductAddNameCategoryActivityPermissionsDispatcher.handleImageUrlFromExternalWithCheck(this)
        } else if (checkImplicitImageUrls()) {
            // because it comes form implicit Uris, check if already login and has shop
            if (validateHasLoginAndShop()) {
                val intent = intent
                if (intent != null && intent.action != null) {
                    when (intent.action) {
                        Intent.ACTION_SEND -> ProductAddNameCategoryActivityPermissionsDispatcher.handleImageUrlImplicitSingleWithCheck(this)
                        Intent.ACTION_SEND_MULTIPLE -> ProductAddNameCategoryActivityPermissionsDispatcher.handleImageUrlImplicitMultipleWithCheck(this)
                    }
                }
            }
        } else { // no image urls, create it directly
            createProductAddFragment()
        }
    }

    private fun checkExplicitImageUrls(): Boolean {
        val intent = intent
        val imageUrlsTemp = intent.getStringArrayListExtra(BaseProductAddEditFragment.EXTRA_IMAGES)
        return imageUrlsTemp != null && imageUrlsTemp.size > 0
    }

    private fun checkImplicitImageUrls(): Boolean {
        val intent = intent
        val action = intent.action
        val type = intent.type ?: return false

        if (!type.startsWith(IMAGE) && !type.startsWith(IMAGE_OR_VIDEO)) {
            return false
        }
        if (Intent.ACTION_SEND == action) {
            return intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) != null
        } else if (Intent.ACTION_SEND_MULTIPLE == action) {
            return intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) != null || CommonUtils.checkCollectionNotNull<ArrayList<Parcelable>>(intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM))
        }
        return false
    }

    private fun validateHasLoginAndShop(): Boolean {
        if (SessionHandler.isV4Login(this)) {
            if (!SessionHandler.isUserHasShop(this)) {
                finish()
                CommonUtils.UniversalToast(baseContext, getString(R.string.title_no_shop))
                return false
            }
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            intent?.run {
                startActivity(intent)
                finish()
                return false
            }
        }
        return true
    }

    private fun processMultipleImage(imageUris: ArrayList<Uri>) {
        showProgressDialog()
        initPresenter()
        productAddImagePresenter?.convertUrisToLocalPaths(imageUris)
    }

    private fun initPresenter() {
        if (productAddImagePresenter == null) {
            productAddImagePresenter = ProductAddImagePresenter()
        }
        productAddImagePresenter!!.attachView(this)
    }

    private fun showProductAddFragment() {
        if (!this@ProductAddNameCategoryActivity.isFinishing) {
            dismissDialog()
            createProductAddFragment()
        }
    }

    override fun onSuccessStoreImageToLocal(imageUrls: ArrayList<String>) {
        this.imageUrls = ArrayList<String>()
        this.imageUrls!!.addAll(imageUrls)
        showProductAddFragment()
    }

    override fun onError(e: Throwable) {
        showProductAddFragment()
    }

    override fun getContext(): Context {
        return this
    }


    fun showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS)
        }
        tkpdProgressDialog!!.showDialog()
    }

    fun dismissDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog!!.dismiss()
        }
    }

    companion object {
        fun createInstance(context: Context?): Intent {
            val intent = Intent(context, ProductAddNameCategoryActivity::class.java)
            return intent
        }

        fun createInstance(context: Context?, productImages: ArrayList<String>): Intent {
            val intent = Intent(context, ProductAddNameCategoryActivity::class.java)
            intent.putStringArrayListExtra(BaseProductAddEditFragment.EXTRA_IMAGES, productImages)
            return intent
        }
    }
}
