package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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
    childViewsLayoutResourceId = LAYOUT,
    showOverlay = true,
    showCloseButton = true,
    showKnob = false,
    clearPadding = true,
    draggable = false,
    bottomSheetTitle = context.getString(R.string.automatic_shipping),
    context = context,
    dismissOnClickOverlay = true
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
        private const val URL_IMAGE_BOTTOM_BACKGROUND =
            "https://images.tokopedia.net/img/img_bottomsheet_dropoff.png"
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
        binding?.run {
            if (listNotes.isNotEmpty()) {
                imageBackground.loadImage(URL_IMAGE_BOTTOM_BACKGROUND)
                imageBackground.visible()
                globalError.gone()
                rvInfo.visible()
            } else {
                imageBackground.gone()
                rvInfo.gone()
                globalError.setType(GlobalError.SERVER_ERROR)
                globalError.visible()
                globalError.errorAction.gone()
            }
        }
    }

    private fun constructList(popUp: PopUp): List<ConfirmShippingNotes> {
        val templateParam = popUp.template?.param
        var listNotes = listOf<ConfirmShippingNotes>()
        templateParam?.apply {
            listNotes = when (popUp.template.code) {
                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_1 -> {
                    templateFmd(
                        learnMoreUrl = this.learnMoreUrl?.data.orEmpty(),
                        learnMoreText = this.learnMoreText?.data.orEmpty(),
                        dropoffUrl = this.dropoffUrl?.data.orEmpty(),
                        dropoffText = this.dropoffText?.data.orEmpty()
                    )
                }

                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_2 -> {
                    templateFmdNotEligible(
                        dropOffUrl = this.dropoffUrl?.data.orEmpty(),
                        dropOffText = this.dropoffText?.data.orEmpty()
                    )
                }

                SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_3 -> templateAutoAwb()
                else -> listOf()
            }
        }

        return listNotes
    }

    private fun templateFmd(
        learnMoreUrl: String,
        learnMoreText: String,
        dropoffUrl: String,
        dropoffText: String
    ) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_minimal_delivery)
                .orEmpty(),
            url = learnMoreUrl,
            urlText = learnMoreText
        ),
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_print_label_and_delivery)
                .orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_cek_gerai).orEmpty(),
            url = dropoffUrl,
            urlText = dropoffText
        )
    )

    private fun templateFmdNotEligible(dropOffUrl: String, dropOffText: String) = listOf(
        ConfirmShippingNotes(
            noteText = context.resources?.getString(R.string.som_dropoff_template_text_print_label_and_delivery)
                .orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_cek_gerai).orEmpty(),
            url = dropOffUrl,
            urlText = dropOffText
        )
    )

    private fun templateAutoAwb() = listOf(
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_print_label_only)
                .orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_no_cost_delivery)
                .orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_no_manual_input_resi)
                .orEmpty()
        ),
        ConfirmShippingNotes(
            context.resources?.getString(R.string.som_dropoff_template_text_auto_confirm).orEmpty()
        )
    )
}
