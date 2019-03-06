package com.tokopedia.broadcast.message.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcastMessageCreateComponent
import com.tokopedia.broadcast.message.data.model.BlastMessageMutation
import com.tokopedia.broadcast.message.data.model.MyProduct
import com.tokopedia.broadcast.message.data.model.ProductPayloadMutation
import com.tokopedia.broadcast.message.view.activity.BroadcastMessagePreviewActivity
import com.tokopedia.broadcast.message.view.adapter.BroadcastMessageProductItemAdapter
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageCreateView
import com.tokopedia.broadcast.message.view.presenter.BroadcastMessageCreatePresenter
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import kotlinx.android.synthetic.main.fragment_broadcast_message_create.*
import javax.inject.Inject

class BroadcastMessageCreateFragment: BaseDaggerFragment(), BroadcastMessageCreateView {

    companion object {
        private const val REQUEST_CODE_IMAGE = 0x01
        private const val REQUEST_CODE_PRODUCT = 0x02
        private const val PARAM_PRODUCT_RESULT = "TKPD_ATTACH_PRODUCT_RESULTS"
        private const val REQUEST_MOVE_PREVIEW = 0x03

        private const val PARAM_PRODUCT_ID = "id"
        private const val PARAM_PRODUCT_NAME = "name"
        private const val PARAM_PRODUCT_PRICE = "price"
        private const val PARAM_PRODUCT_URL = "url"
        private const val PARAM_PRODUCT_THUMBNAIL = "thumbnail"
    }

    @Inject lateinit var presenter: BroadcastMessageCreatePresenter
    private val router: BroadcastMessageRouter? by lazy {
        activity?.application as? BroadcastMessageRouter
    }
    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }
    private var shopName: String = ""
    private var savedLocalImageUrl: String? = null
    private val selectedProducts = mutableListOf<MyProduct>()
    private val productIds = mutableListOf<Int>()
    private val hashProducList = ArrayList<HashMap<String, String>>()
    private val productAdapter = BroadcastMessageProductItemAdapter({gotoAddProduct()}){
            productId, position -> productIds.remove(productId); selectedProducts.removeAt(position)
                                    hashProducList.removeAt(position)}
    var isShowDialogWhenBack: Boolean = false

    private fun gotoAddProduct() {
        val app = activity?.application
        if (app == null) return

        if (app is BroadcastMessageRouter && context != null){
            startActivityForResult(app.getBroadcastMessageAttachProductIntent(context!!, userSession.shopId, shopName,
                    !userSession.shopId.isNullOrEmpty(), productIds, hashProducList), REQUEST_CODE_PRODUCT)
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerBroadcastMessageCreateComponent.builder()
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
            router?.run {
                sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                        BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                        BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_TOGGLE_ATTACH_PRODUCT, "")
            }
            isShowDialogWhenBack = true
            list_product_upload.visibility = if (isChecked) View.VISIBLE else View.GONE
            needEnabledSubmitButton()
        }
        edit_text_message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isShowDialogWhenBack = true
                needEnabledSubmitButton()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        button_save.setOnClickListener {
            if (switch_upload_product.isChecked && productIds.isEmpty()){
                ToasterError.make(view, getString(R.string.empty_attached_product),
                        BaseToaster.LENGTH_INDEFINITE).setAction(R.string.OK){
                    router?.run {
                        sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_CONFIRMATION,
                                BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                                BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_ERROR_ATTACH_PRODUCT,
                                BroadcastMessageConstant.VALUE_GTM_EVENT_LABEL_ERROR_OK)
                    }
                }.show()
            } else {
                moveToPreview()
            }
        }

        val spannableBuilder = SpannableStringBuilder()
        spannableBuilder.append(getString(R.string.attach_my_product))
        spannableBuilder.append(" ")
        spannableBuilder.append(SpannableString(getString(R.string.optional)).apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.black_38)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        })

        title_switch_product.setText(spannableBuilder, TextView.BufferType.SPANNABLE)

        presenter.getShopInfo()
        needEnabledSubmitButton()
    }

    private fun moveToPreview() {
        var productsPayload = listOf<ProductPayloadMutation>()
        if (switch_upload_product.isChecked){
            productsPayload = selectedProducts.map {
                val profile = ProductPayloadMutation.ProductProfileMutation(it.productName, it.productPrice, it.productThumbnail, it.productUrl)
                ProductPayloadMutation(it.productId, profile)
            }
        }
        val modelMutation = BlastMessageMutation(edit_text_message.text.toString(), "", savedLocalImageUrl!!,
                switch_upload_product.isChecked, productsPayload.toTypedArray())
        context?.let { startActivityForResult(BroadcastMessagePreviewActivity.createIntent(it, modelMutation), REQUEST_MOVE_PREVIEW) }
    }

    private fun openImagePicker() {
        router?.run {
            sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                    BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_CLICK_IMG_UPLOAD, "")
        }
        context?.let {
            val builder = ImagePickerBuilder(getString(R.string.bm_choose_picture),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_INSTAGRAM),
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
        if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode == REQUEST_CODE_IMAGE) {
                router?.run {
                    sendEventTracking(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                            BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                            BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_PICK_IMG, "")
                }

                val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    savedLocalImageUrl = imageUrlOrPathList[0]
                    updateBackgroundImage()
                    isShowDialogWhenBack = true
                }
            } else if (requestCode == REQUEST_CODE_PRODUCT){
                val productList = data.getSerializableExtra(PARAM_PRODUCT_RESULT) as List<HashMap<String, String>>
                selectedProducts.clear()
                productIds.clear()
                hashProducList.clear()

                hashProducList.addAll(productList)
                productList.forEach {
                    productIds.add(it.get("id")?.toInt() ?: -1)
                    selectedProducts.add(MyProduct(productId = it.get(PARAM_PRODUCT_ID)?.toInt() ?: -1,
                            productName = it.get(PARAM_PRODUCT_NAME) ?: "",
                            productPrice = it.get(PARAM_PRODUCT_PRICE) ?: "Rp0",
                            productThumbnail = it.get(PARAM_PRODUCT_THUMBNAIL) ?: "",
                            productUrl = it.get(PARAM_PRODUCT_URL) ?: ""))
                }

                productAdapter.clearProducts()
                productAdapter.addProducts(selectedProducts)
                needEnabledSubmitButton()
                isShowDialogWhenBack = true
            } else if (requestCode == REQUEST_MOVE_PREVIEW){
                val needRefresh = data.getBooleanExtra(BroadcastMessageConstant.PARAM_NEED_REFRESH, false)
                val isNeedRefresh = Intent().putExtra(BroadcastMessageConstant.PARAM_NEED_REFRESH, needRefresh)
                activity?.run {
                    setResult(Activity.RESULT_OK, isNeedRefresh)
                    finish()
                }
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
        needEnabledSubmitButton()
    }

    private fun needEnabledSubmitButton(){
        var valid = true
        valid = valid && !savedLocalImageUrl.isNullOrEmpty()
        valid = valid && !edit_text_message.text.toString().isEmpty()

        button_save.isEnabled = valid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onErrorGetShopInfo(e: Throwable) {}

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        total_follower.text = resources.getQuantityString(R.plurals.template_follower, shopInfo.info.shopTotalFavorit.toInt(),
                shopInfo.info.shopTotalFavorit.toInt())
        shopName = shopInfo.info.shopName
    }
}