package com.tokopedia.broadcast.message.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcasteMessageCreateComponent
import com.tokopedia.broadcast.message.data.model.MyProduct
import com.tokopedia.broadcast.message.view.adapter.BroadcastMessageProductItemAdapter
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageCreateView
import com.tokopedia.broadcast.message.view.presenter.BroadcastMessageCreatePresenter
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import kotlinx.android.synthetic.main.fragment_broadcast_message_create.*
import javax.inject.Inject

class BroadcastMessageCreateFragment: BaseDaggerFragment(), BroadcastMessageCreateView {

    companion object {
        private const val REQUEST_CODE_IMAGE = 0x01
    }

    @Inject lateinit var presenter: BroadcastMessageCreatePresenter
    private var savedLocalImageUrl: String? = null
    private val productAdapter = BroadcastMessageProductItemAdapter()

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerBroadcasteMessageCreateComponent.builder()
                .broadcastMessageComponent(getComponent(BroadcastMessageComponent::class.java))
                .build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_broadcast_message_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        total_follower.requestFocus()
        bg_image.setOnClickListener { openImagePicker() }
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        val dividerDrawable = context?.let {  ContextCompat.getDrawable(it, R.drawable.product_attach_divider) }
        dividerDrawable?.let { itemDecoration.setDrawable(it)
                            list_product_upload.addItemDecoration(itemDecoration)}
        list_product_upload.adapter = productAdapter
        list_product_upload.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        switch_upload_product.setOnCheckedChangeListener { _, isChecked ->
            list_product_upload.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        productAdapter.addProduct(MyProduct(productName = "Oneplus 6", productPrice = "Rp7.700.000"))
        presenter.getShopInfo()
    }

    private fun openImagePicker() {
        context?.let {
            val builder = ImagePickerBuilder(getString(R.string.bm_choose_picture),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA, ImagePickerTabTypeDef.TYPE_INSTAGRAM),
                    GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                            false, null), null)
            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_CODE_IMAGE){
            val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
            if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                savedLocalImageUrl = imageUrlOrPathList[0]
                updateBackgroundImage()
            }
        }
    }

    private fun updateBackgroundImage() {
        if (savedLocalImageUrl == null){
            bg_image.setImageResource(R.drawable.ic_upload_image)
            bg_image.scaleType = ImageView.ScaleType.CENTER
        } else {
            ImageHandler.LoadImage(bg_image, savedLocalImageUrl)
            bg_image.scaleType = ImageView.ScaleType.CENTER_CROP
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                bg_image.clipToOutline = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onErrorGetShopInfo(e: Throwable) {}

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        total_follower.text = resources.getQuantityString(R.plurals.template_follower, shopInfo.info.shopTotalFavorit.toInt(),
                shopInfo.info.shopTotalFavorit.toInt())
    }
}