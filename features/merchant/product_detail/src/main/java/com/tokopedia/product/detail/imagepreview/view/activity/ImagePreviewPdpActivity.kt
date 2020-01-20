package com.tokopedia.product.detail.imagepreview.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.imagepreview.data.ImagePreviewTracking
import com.tokopedia.product.detail.imagepreview.di.DaggerImagePreviewPdpComponent
import com.tokopedia.product.detail.imagepreview.view.listener.ImagePreviewPdpView
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_image_preview_pdp.btnAddToWishlist
import kotlinx.android.synthetic.main.activity_image_preview_pdp.progressBar
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * created by rival on 07/11/19
 * image preview with wishlist button
 */

class ImagePreviewPdpActivity : ImagePreviewActivity(), ImagePreviewPdpView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    val viewModel by lazy { viewModelProvider.get(ImagePreviewPdpViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var imagePreviewTracking: ImagePreviewTracking

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var productId: String = "0"
    private var isWishlisted by Delegates.notNull<Boolean>()

    override fun layoutId(): Int {
        return R.layout.activity_image_preview_pdp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extraData = intent?.extras
        if (extraData != null) {
            productId = extraData.getString(PRODUCT_ID) ?: "0"
            isWishlisted = extraData.getBoolean(IS_WISHLISTED, false)
        }

        initInjector()
        updateView()
        initListener()
    }

    private fun initInjector() {
        DaggerImagePreviewPdpComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun updateView() {
        findViewById<Button>(R.id.ivDownload)?.hide()

        val isCanShowing = remoteConfig.getBoolean(KEY_WISHLIST_BUTTON, false)
        if (isCanShowing) {
            btnAddToWishlist?.show()
        } else {
            btnAddToWishlist?.hide()
        }

        if (userSession.isLoggedIn) {
            if (isWishlisted) {
                btnAddToWishlist?.text = resources.getString(R.string.image_preview_remove_wishlist)
            } else {
                btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
            }
        } else {
            btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
        }
    }

    private fun initListener() {
        btnAddToWishlist?.setOnClickListener {
            if (userSession.isLoggedIn) {
                if (isWishlisted) {
                    removeWishlist()
                } else {
                    addWishlist()
                }
            } else {
                gotoLogin()
                imagePreviewTracking.onAddWishlistNonLogin()
            }
        }

        findViewById<ImageView>(R.id.ivClose)?.setOnClickListener {
            setResult()
        }
    }

    override fun onBackPressed() {
        setResult()
    }

    private fun setResult() {
        val intent = Intent()
        intent.putExtra(RESPONSE_CODE_IMAGE_RPEVIEW, isWishlisted)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    addWishlist()
                    startActivity(RouteManager.getIntent(applicationContext, ApplinkConst.WISHLIST))
                    finish()
                }
            }
        }
    }

    override fun addWishlist() {
        showLoading()
        viewModel.addWishList(
                productId,
                onSuccessAddWishlist = {
                    hideLoading()
                    onSuccessAddWishlist()
                    updateView()
                    imagePreviewTracking.onSuccessAdd()
                },
                onErrorAddWishList = {
                    hideLoading()
                    onErrorAddWishlist(Throwable(it))
                }
        )
    }

    override fun removeWishlist() {
        showLoading()
        viewModel.removeWishList(
                productId,
                onSuccessRemoveWishlist = {
                    hideLoading()
                    onSuccessRemoveWishlist()
                    updateView()
                    imagePreviewTracking.onSuccessRemove()
                },
                onErrorRemoveWishList = {
                    hideLoading()
                    onErrorRemoveWishlist(Throwable(it))
                }
        )
    }

    override fun gotoLogin() {
        startActivityForResult(RouteManager.getIntent(applicationContext, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
    }

    override fun showLoading() {
        progressBar?.show()
    }

    override fun hideLoading() {
        progressBar?.hide()
    }

    override fun onSuccessAddWishlist() {
        isWishlisted = true
        showMessage(resources.getString(R.string.image_preview_add_wishlist_success))
    }

    override fun onSuccessRemoveWishlist() {
        isWishlisted = false
        showMessage(resources.getString(R.string.image_preview_remove_wishlist_success))
    }

    override fun onErrorAddWishlist(throwable: Throwable) {
        showErrorMessage(throwable.message.toString())
    }

    override fun onErrorRemoveWishlist(throwable: Throwable) {
        showErrorMessage(throwable.message.toString())
    }

    override fun showMessage(message: String) {
        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
        Toaster.make(rootView, message, Toaster.toasterLength, Toaster.TYPE_NORMAL)
    }

    override fun showErrorMessage(message: String) {
        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
        Toaster.make(rootView, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
    }

    companion object {

        private const val KEY_WISHLIST_BUTTON = "android_customer_image_preview_wishlist_pdp"

        private const val PRODUCT_ID = "productId"
        private const val IS_WISHLISTED = "isWishlisted"

        private const val REQUEST_CODE_LOGIN = 561
        const val RESPONSE_CODE_IMAGE_RPEVIEW = "responseImagePreview"

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context,
                productId: String,
                isWishlisted: Boolean,
                imageUris: ArrayList<String>,
                imageDesc: ArrayList<String>? = null,
                position: Int = 0,
                title: String? = null,
                description: String? = null): Intent {
            val intent = Intent(context, ImagePreviewPdpActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putString(PRODUCT_ID, productId)
            bundle.putBoolean(IS_WISHLISTED, isWishlisted)
            intent.putExtras(bundle)
            return intent
        }
    }
}