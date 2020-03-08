package com.tokopedia.product.addedit.description.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.model.VideoLinkModel
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory
import com.tokopedia.product.addedit.tooltip.model.TooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.addedit.common.util.CurrencyTypeDef
import com.tokopedia.product.addedit.common.util.ProductExtraConstant
import com.tokopedia.product.addedit.stock.view.model.ProductStock

class AddEditProductDescriptionFragment : BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
        VideoLinkTypeFactory.VideoLinkListener {
    private var videoId = 0
    private var tooltipBottomSheet: TooltipBottomSheet<TooltipModel, TooltipTypeFactory>? = null

    @CurrencyTypeDef
    private var selectedCurrencyType: Int = CurrencyTypeDef.TYPE_IDR

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
        adapter.data[position].inputName = url
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
            showDescriptionTips()
        }

        tvAddVariant.setOnClickListener {
            showEditPriceWhenHasVariantDialog()
        }
    }

    override fun loadData(page: Int) {
        val videoLinkModels: ArrayList<VideoLinkModel> = ArrayList()
        videoLinkModels.add(VideoLinkModel(page, "", "https://placekitten.com/200/200"))
        super.renderList(videoLinkModels)

        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    private fun showDescriptionTips() {
        val choosingTipsFactory = TooltipTypeFactory()
        tooltipBottomSheet = TooltipBottomSheet(choosingTipsFactory)
        tooltipBottomSheet?.apply {
            setTitle("Tips memilih produk")
        }
        val tips: ArrayList<TooltipModel> = ArrayList()
        tips.add(TooltipModel(1, "Pastikan kualitas image tidak pecah", "Kamu bisa cek kembali fotomu ketika dilihat lebih detail", "https://placekitten.com/300/300"))
        tips.add(TooltipModel(2, "Atur fotomu semenarik mungkin", "tentukan foto yang menarik menurutmu agar pembeli dengan mudah menemukan", "https://placekitten.com/300/301"))
        tips.add(TooltipModel(3, "Tentukan komposisi dan sudut foto", "kamu bisa mengatur komposisi objek pada saat foto dan sudut menarik", "https://placekitten.com/300/302"))

        tooltipBottomSheet?.setItemMenuList(tips)
        tooltipBottomSheet?.show(fragmentManager!!, null)
    }

    private fun showEditPriceWhenHasVariantDialog(){
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
            intent?.run {
                putExtra(ProductExtraConstant.EXTRA_CURRENCY_TYPE, selectedCurrencyType)
                putExtra(ProductExtraConstant.EXTRA_DEFAULT_PRICE, 0.0)
                putExtra(ProductExtraConstant.EXTRA_STOCK_TYPE, ProductStock())
                putExtra(ProductExtraConstant.EXTRA_IS_OFFICIAL_STORE, false)
                putExtra(ProductExtraConstant.EXTRA_DEFAULT_SKU, "")
                putExtra(ProductExtraConstant.EXTRA_NEED_RETAIN_IMAGE, false)
                putExtra(ProductExtraConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV1, true)
                putExtra(ProductExtraConstant.EXTRA_HAS_ORIGINAL_VARIANT_LV2, false)
                putExtra(ProductExtraConstant.EXTRA_HAS_WHOLESALE, false)
                putExtra(ProductExtraConstant.EXTRA_IS_ADD, false)
                startActivityForResult(this, REQUEST_CODE_VARIANT)
            }
        }
    }

    companion object {
        const val MAX_VIDEOS = 3

        const val REQUEST_CODE_VARIANT = 9
    }

}
