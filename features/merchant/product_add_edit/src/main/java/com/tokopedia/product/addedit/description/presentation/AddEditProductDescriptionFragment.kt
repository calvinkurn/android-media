package com.tokopedia.product.addedit.description.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.model.VideoLinkModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*

class AddEditProductDescriptionFragment : BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
        VideoLinkTypeFactory.VideoLinkListener {

    private var videoId = 0

    override fun getAdapterTypeFactory(): VideoLinkTypeFactory {
        val videoLinkTypeFactory = VideoLinkTypeFactory()
        videoLinkTypeFactory.setVideoLinkListener(this)

        return videoLinkTypeFactory
    }

    override fun onDeleteClicked(videoLinkModel: VideoLinkModel, position: Int) {
        adapter.data.removeAt(position)
        adapter.notifyDataSetChanged()
        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    override fun onTextChanged(url: String, position: Int) {
        adapter.data[position].inputUrl = url
    }

    override fun onItemClicked(t: VideoLinkModel?) {
        //no op
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldDescription.textFieldInput.setSingleLine(false)
        textFieldDescription.textFieldInput.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION

        textViewAddVideo.setOnClickListener {
            videoId += 1
            loadData(videoId)
        }

        layoutDescriptionTips.setOnClickListener {
            showDescriptionTips()
        }

        layoutVariantTips.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant.setOnClickListener {
            showVariantDialog()
        }
    }

    private fun showDescriptionTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<NumericTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_description_tips)
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_1)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_2)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_3)))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    private fun showVariantTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<NumericTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_variant_tips)
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_1)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_2)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_3)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_4)))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    override fun loadData(page: Int) {
        val videoLinkModels: ArrayList<VideoLinkModel> = ArrayList()
        videoLinkModels.add(VideoLinkModel(page, "", TEST_IMAGE_URL))
        super.renderList(videoLinkModels)

        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    private fun showVariantDialog(){
        activity?.let {
            val productVariantByCatModelList: ArrayList<String> = ArrayList()
            productVariantByCatModelList.add(TEST_VARIANT)
            val cacheManager = SaveInstanceCacheManager(it, true).apply {
                put(EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList) // must using json
                put(EXTRA_PRODUCT_VARIANT_SELECTION, "") // must using json
                put(EXTRA_CURRENCY_TYPE, TYPE_IDR)
                put(EXTRA_DEFAULT_PRICE, 0.0)
                put(EXTRA_STOCK_TYPE, "")
                put(EXTRA_IS_OFFICIAL_STORE, false)
                put(EXTRA_DEFAULT_SKU, "")
                put(EXTRA_NEED_RETAIN_IMAGE, false)
                put(EXTRA_PRODUCT_SIZECHART, null) // must using json
                put(EXTRA_HAS_ORIGINAL_VARIANT_LV1, true)
                put(EXTRA_HAS_ORIGINAL_VARIANT_LV2, false)
                put(EXTRA_HAS_WHOLESALE, false)
                put(EXTRA_IS_ADD, false)
            }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
            intent?.run {
                putExtra(EXTRA_VARIANT_CACHE_ID, cacheManager.id)
                putExtra(EXTRA_IS_USING_CACHE_MANAGER, true)
                startActivityForResult(this, REQUEST_CODE_VARIANT)
            }
        }
    }

    companion object {
        const val MAX_VIDEOS = 3
        const val REQUEST_CODE_VARIANT = 0

        const val TYPE_IDR = 1
        const val TYPE_USD = 2

        const val EXTRA_PRODUCT_VARIANT_SELECTION = "EXTRA_PRODUCT_VARIANT_SELECTION"
        const val EXTRA_PRODUCT_SIZECHART = "EXTRA_PRODUCT_SIZECHART"

        const val EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST"
        const val EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE"
        const val EXTRA_DEFAULT_PRICE = "EXTRA_PRICE"
        const val EXTRA_STOCK_TYPE = "EXTRA_STOCK_TYPE"
        const val EXTRA_IS_OFFICIAL_STORE = "EXTRA_IS_OFFICIAL_STORE"
        const val EXTRA_NEED_RETAIN_IMAGE = "EXTRA_NEED_RETAIN_IMAGE"
        const val EXTRA_DEFAULT_SKU = "EXTRA_DEFAULT_SKU"
        const val EXTRA_HAS_ORIGINAL_VARIANT_LV1 = "EXTRA_HAS_ORI_VAR_LV1"
        const val EXTRA_HAS_ORIGINAL_VARIANT_LV2 = "EXTRA_HAS_ORI_VAR_LV2"
        const val EXTRA_HAS_WHOLESALE = "EXTRA_HAS_WHOLESALE"
        const val EXTRA_IS_ADD = "EXTRA_IS_ADD"

        const val EXTRA_VARIANT_CACHE_ID = "variant_cache_id"
        const val EXTRA_IS_USING_CACHE_MANAGER = "is_using_cache_manager"

        // TODO faisalramd
        const val TEST_VARIANT = "{\"variant_id\":1,\"name\":\"Warna\",\"identifier\":\"colour\",\"status\":2,\"has_unit\":0,\"units\":[{\"unit_id\":0,\"name\":\"\",\"short_name\":\"\",\"values\":[{\"value_id\":1,\"value\":\"Putih\",\"hex_code\":\"#ffffff\",\"icon\":\"\"},{\"value_id\":2,\"value\":\"Hitam\",\"hex_code\":\"#000000\",\"icon\":\"\"}]}]}"
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

}
