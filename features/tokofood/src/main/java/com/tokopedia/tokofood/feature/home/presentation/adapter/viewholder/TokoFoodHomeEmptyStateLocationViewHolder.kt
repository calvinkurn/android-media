package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodEmptyStateLocationBinding
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.Typography

class TokoFoodHomeEmptyStateLocationViewHolder (
    itemView: View,
    private val tokoFoodEmptyStateListener: TokoFoodHomeEmptyStateLocationListener? = null
): AbstractViewHolder<TokoFoodHomeEmptyStateLocationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_empty_state_location

        const val IMG_STATIC_URI_NO_PIN_POIN = "https://images.tokopedia.net/img/ic-tokofood_home_no_pin_poin.png"
        const val IMG_STATIC_URI_NO_ADDRESS = "https://images.tokopedia.net/img/ic_tokofood_home_no_address.png"
        const val IMG_STATIC_URI_OUT_OF_COVERAGE = "https://images.tokopedia.net/img/ic_tokofood_home_out_of_coverage.png"
    }

    private var binding: ItemTokofoodEmptyStateLocationBinding? by viewBinding()
    private var imgEmptyStateLocation: ImageView? = null
    private var tgTitleEmptyStateLocation: Typography? = null
    private var tgDescEmptyStateLocation: Typography? = null
    private var btnEmptyStateLocation: UnifyButton? = null
    private var tgToHomeEmptyStateLocation: Typography? = null

    override fun bind(element: TokoFoodHomeEmptyStateLocationUiModel) {
        setupEmptyState()
        when(element.id) {
            EMPTY_STATE_NO_PIN_POINT -> bindNoPinPoin()
            EMPTY_STATE_NO_ADDRESS -> bindNoAddress()
            EMPTY_STATE_OUT_OF_COVERAGE -> bindOutOfCoverage()
        }
    }

    private fun setupEmptyState() {
        imgEmptyStateLocation = binding?.imgEmptyStateLocation
        tgTitleEmptyStateLocation = binding?.tgEmptyStateLocationTitle
        tgDescEmptyStateLocation = binding?.tgEmptyStateLocationDesc
        btnEmptyStateLocation = binding?.btnEmptyStateLocation
        tgToHomeEmptyStateLocation = binding?.tgEmptyStateLocationToHome
    }

    private fun bindNoPinPoin() {
        tgToHomeEmptyStateLocation?.hide()
        tokoFoodEmptyStateListener?.viewNoPinPoin()
        imgEmptyStateLocation?.loadImage(IMG_STATIC_URI_NO_PIN_POIN)
        tgTitleEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_pin_poin_title)
        tgDescEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_pin_poin_desc)
        btnEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_pin_poin_button)
        btnEmptyStateLocation?.setOnClickListener {
            tokoFoodEmptyStateListener?.onClickSetPinPoin()
        }
    }

    private fun bindNoAddress() {
        tgToHomeEmptyStateLocation?.show()
        imgEmptyStateLocation?.loadImage(IMG_STATIC_URI_NO_ADDRESS)
        tgTitleEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_address_title)
        tgDescEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_address_desc)
        btnEmptyStateLocation?.text = itemView.resources.getString(R.string.home_no_address_button)
        btnEmptyStateLocation?.setOnClickListener {
            tokoFoodEmptyStateListener?.onClickSetAddress()
        }
        tgToHomeEmptyStateLocation?.setOnClickListener {
            tokoFoodEmptyStateListener?.onClickBackToHome()
        }
    }

    private fun bindOutOfCoverage() {
        val title = itemView.resources.getString(R.string.home_out_of_coverage_title)
        val desc = itemView.resources.getString(R.string.home_out_of_coverage_desc)

        tgToHomeEmptyStateLocation?.show()
        tokoFoodEmptyStateListener?.viewOutOfCoverage()
        imgEmptyStateLocation?.loadImage(IMG_STATIC_URI_OUT_OF_COVERAGE)
        tgTitleEmptyStateLocation?.text = title
        tgDescEmptyStateLocation?.text = desc
        btnEmptyStateLocation?.text = itemView.resources.getString(R.string.home_out_of_coverafe_button)
        btnEmptyStateLocation?.setOnClickListener {
            tokoFoodEmptyStateListener?.onClickSetAddressInCoverage("TODO_ERROR_STATE", title, desc)
        }
        tgToHomeEmptyStateLocation?.setOnClickListener {
            tokoFoodEmptyStateListener?.onClickBackToHome()
        }
    }

    interface TokoFoodHomeEmptyStateLocationListener {
        fun onClickSetPinPoin()
        fun onClickSetAddress()
        fun onClickSetAddressInCoverage(errorState: String, title: String, desc: String)
        fun onClickBackToHome()
        fun viewOutOfCoverage()
        fun viewNoPinPoin()
    }
}