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
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ticker.TouchViewPager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ActivityImagePreviewPdpBinding
import com.tokopedia.product.detail.imagepreview.data.ImagePreviewTracking
import com.tokopedia.product.detail.imagepreview.di.DaggerImagePreviewPdpComponent
import com.tokopedia.product.detail.imagepreview.view.listener.ImagePreviewPdpView
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import java.util.*
import javax.inject.Inject

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
    private var isWishlisted: Boolean = false
    private var shopId: String = ""

    private var binding: ActivityImagePreviewPdpBinding? = null

    override fun layoutId(): Int {
        return R.layout.activity_image_preview_pdp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewPdpBinding.bind(findViewById(R.id.imagePreviewPdpContainer))

        val extraData = intent?.extras
        if (extraData != null) {
            productId = extraData.getString(PRODUCT_ID) ?: "0"
            isWishlisted = extraData.getBoolean(IS_WISHLISTED, false)
            shopId = extraData.getString(SHOP_ID, "")
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
        findViewById<Button>(com.tokopedia.imagepreview.R.id.ivDownload)?.hide()

        if (viewModel.isShopOwner(shopId)) {
            binding?.btnAddToWishlist?.hide()
            return
        }

        val isCanShowing = remoteConfig.getBoolean(KEY_WISHLIST_BUTTON, false)
        if (isCanShowing) {
            binding?.btnAddToWishlist?.show()
        } else {
            binding?.btnAddToWishlist?.hide()
        }

        if (userSession.isLoggedIn) {
            if (isWishlisted) {
                binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_remove_wishlist)
            } else {
                binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
            }
        } else {
            binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
        }
    }

    private fun initListener() {
        binding?.btnAddToWishlist?.setOnClickListener {
            if (userSession.isLoggedIn) {
                if (isWishlisted) {
                    removeWishlistV2(this)
                } else {
                    addWishlistV2(this)
                }
            } else {
                gotoLogin()
                imagePreviewTracking.onAddWishlistNonLogin()
            }
        }

        findViewById<TouchViewPager>(com.tokopedia.imagepreview.R.id.viewPager)?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var lastPosition = 0
            override fun onPageSelected(position: Int) {
                val swipeDirection = if (lastPosition > position) IMAGE_SWIPE_DIRECTION_LEFT else IMAGE_SWIPE_DIRECTION_RIGHT
                imagePreviewTracking.onImageSwipe(productId, swipeDirection, position)
                lastPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        findViewById<ImageView>(com.tokopedia.imagepreview.R.id.ivClose)?.setOnClickListener {
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
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    addWishlistV2(applicationContext)
                    startActivity(RouteManager.getIntent(applicationContext, ApplinkConst.WISHLIST))
                    finish()
                }
            }
        }
    }

    private fun isProductIdValid(productId: String): Boolean {
        return productId.isNotEmpty() && productId.matches(Regex(ImagePreviewPdpViewModel.PATTERN_REGEX))
    }

    override fun addWishlistV2(context: Context) {
        if (isProductIdValid(productId)) {
            showLoading()
            viewModel.addWishListV2(
                productId,
                object : WishlistV2ActionListener {
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        val errorMsg = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, rootView)
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, rootView)
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                    override fun onSuccessRemoveWishlist(result: DeleteWishlistV2Response.Data.WishlistRemoveV2, productId: String) {}
                }
            )
        } else {
            val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
            val errorMsg = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context,
                Throwable(resources.getString(com.tokopedia.wishlist_common.R.string.add_to_wishlist_invalid)))
            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, rootView)
        }
    }

    override fun removeWishlistV2(context: Context) {
        if (isProductIdValid(productId)) {
            showLoading()
            viewModel.removeWishListV2(productId,
                object: WishlistV2ActionListener{
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {}

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2, productId: String) {}

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        val errorMsg = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, rootView)
                    }

                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, rootView)
                    }
                })
        } else {
            val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
            val errorMsg = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context,
                Throwable(resources.getString(com.tokopedia.wishlist_common.R.string.add_to_wishlist_invalid)))
            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, rootView)
        }
    }

    override fun gotoLogin() {
        startActivityForResult(RouteManager.getIntent(applicationContext, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
    }

    override fun showLoading() {
        binding?.progressBar?.show()
    }

    override fun hideLoading() {
        binding?.progressBar?.hide()
    }

    override fun onSuccessAddWishlist() {
        isWishlisted = true
        showMessage(resources.getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg))
    }

    override fun onSuccessRemoveWishlist() {
        isWishlisted = false
        showMessage(resources.getString(com.tokopedia.wishlist_common.R.string.on_success_remove_from_wishlist_msg))
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
        Toaster.make(rootView, message, Toaster.toasterLength, TYPE_ERROR)
    }

    companion object {

        private const val KEY_WISHLIST_BUTTON = "android_customer_image_preview_wishlist_pdp"

        private const val SHOP_ID = "shopId"
        private const val PRODUCT_ID = "productId"
        private const val IS_WISHLISTED = "isWishlisted"
        private const val IMAGE_SWIPE_DIRECTION_LEFT = "left"
        private const val IMAGE_SWIPE_DIRECTION_RIGHT = "right"

        private const val REQUEST_CODE_LOGIN = 561
        const val RESPONSE_CODE_IMAGE_RPEVIEW = "responseImagePreview"

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context,
                shopId: String,
                productId: String,
                isWishlisted: Boolean,
                imageUris: ArrayList<String>,
                imageDesc: ArrayList<String>? = null,
                position: Int = 0,
                title: String? = null,
                description: String? = null,
                disableDownload: Boolean = true): Intent {
            val intent = Intent(context, ImagePreviewPdpActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putString(SHOP_ID, shopId)
            bundle.putString(PRODUCT_ID, productId)
            bundle.putBoolean(IS_WISHLISTED, isWishlisted)
            bundle.putBoolean(DISABLE_DOWNLOAD, disableDownload)
            intent.putExtras(bundle)
            return intent
        }
    }
}