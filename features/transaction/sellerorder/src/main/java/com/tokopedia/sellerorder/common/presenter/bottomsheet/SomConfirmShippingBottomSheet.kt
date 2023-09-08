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
        fun instance(context: Context?, view: View?, popUp: PopUp) {
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
    }

    private var bottomSheetAdapter: SomBottomSheetConfirmShippingAdapter? = null

    override fun bind(view: View): PartialInfoLayoutBinding {
        return PartialInfoLayoutBinding.bind(view)
    }

    override fun setupChildView() {
        bottomSheetAdapter = SomBottomSheetConfirmShippingAdapter()
        binding?.apply {
            rvInfo.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = bottomSheetAdapter
            }
        }
    }

    fun setList(popUp: PopUp) {
        val listNotes = constructList(popUp)
        bottomSheetAdapter?.updateListInfo(listNotes)
        binding?.imageBackground?.loadImage("https://images.tokopedia.net/img/img_bottomsheet_dropoff.png")
    }

    private fun constructList(popUp: PopUp): List<ConfirmShippingNotes> {
        val templateParam = popUp.template.param
        return when (popUp.template.code) {
            SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_1 -> template1(templateParam)
            SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_2 -> template2(templateParam)
            SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_3 -> template3()
            else -> listOf()
        }
    }

    private fun template1(templateParam: PopUp.Template.Params) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_1).orEmpty(),
            url = templateParam.learnMoreUrl.data,
            urlText = templateParam.learnMoreText.data
        ),
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_3).orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_2).orEmpty(),
            url = templateParam.dropoffUrl.data,
            urlText = templateParam.dropoffText.data
        )
    )

    private fun template2(templateParam: PopUp.Template.Params) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_3).orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_2).orEmpty(),
            url = templateParam.dropoffUrl.data,
            urlText = templateParam.dropoffText.data
        )
    )

    private fun template3() = listOf(
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_4).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_5).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_6).orEmpty()),
        ConfirmShippingNotes(context.resources?.getString(R.string.som_dropoff_template_text_7).orEmpty())
    )
}
