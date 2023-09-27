package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.presenter.SomBottomSheetConfirmShippingAdapter
import com.tokopedia.sellerorder.common.presenter.model.ConfirmShippingNotes
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.util.SomConsts.SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_1
import com.tokopedia.sellerorder.common.util.SomConsts.SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_2
import com.tokopedia.sellerorder.common.util.SomConsts.SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_3
import com.tokopedia.sellerorder.databinding.PartialInfoLayoutBinding

class SomConfirmShippingBottomSheet(context: Context) : SomBottomSheet<PartialInfoLayoutBinding>(
    LAYOUT, true, true, false, true, false, context.getString(R.string.automatic_shipping), context, true
) {

    companion object {
        fun show(context: Context?, view: View?, popUp: PopUp) {
            view?.apply {
                if (view is ViewGroup) {
                    context?.apply {
                        SomConfirmShippingBottomSheet(context).apply {
                            init(view)
                            setList(popUp)
                            show()
                        }
                    }
                }
            }
        }

        private val LAYOUT = R.layout.partial_info_layout
        private const val URL_IMAGE_BOTTOM_BACKGROUND = "https://images.tokopedia.net/img/img_bottomsheet_dropoff.png"
    }

    private var bottomSheetAdapter: SomBottomSheetConfirmShippingAdapter? = null

    override fun bind(view: View): PartialInfoLayoutBinding {
        return PartialInfoLayoutBinding.bind(view)
    }

    override fun setupChildView() {
        bottomSheetAdapter = SomBottomSheetConfirmShippingAdapter()
        binding?.rvInfo?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = bottomSheetAdapter
        }
    }

    fun setList(popUp: PopUp) {
        val listNotes = constructList(popUp)
        bottomSheetAdapter?.updateListInfo(listNotes)
        binding?.imageBackground?.loadImage(URL_IMAGE_BOTTOM_BACKGROUND)
    }

    private fun constructList(popUp: PopUp): List<ConfirmShippingNotes> {
        val templateParam = popUp.template?.param
        var listNotes = listOf<ConfirmShippingNotes>()
        // todo ask ka tama how ios handle error here
        templateParam?.apply {
            listNotes = when (popUp.template.code) {
                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_1 -> templateFmd(templateParam)
                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_2 -> templateFmdNotEligible(templateParam)
                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_3 -> templateAutoAwb()
                else -> listOf()
            }
        }

        return listNotes
    }

    private fun templateFmd(templateParam: PopUp.Template.Params) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_minimal_delivery).orEmpty(),
            url = templateParam.learnMoreUrl.data,
            urlText = templateParam.learnMoreText.data
        ),
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_print_label_and_delivery).orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_cek_gerai).orEmpty(),
            url = templateParam.dropoffUrl.data,
            urlText = templateParam.dropoffText.data
        )
    )

    private fun templateFmdNotEligible(templateParam: PopUp.Template.Params) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_print_label_and_delivery).orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_cek_gerai).orEmpty(),
            url = templateParam.dropoffUrl.data,
            urlText = templateParam.dropoffText.data
        )
    )

    private fun templateAutoAwb() = listOf(
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_print_label_only).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_no_cost_delivery).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_no_manual_input_resi).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_auto_confirm).orEmpty())
    )
}
