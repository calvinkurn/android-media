package com.tokopedia.search.result.mps.violationstate

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchMpsViolationStateBinding
import com.tokopedia.utils.view.binding.viewBinding

class ViolationStateViewHolder(
    itemView: View,
    private val violationStateListener: ViolationStateListener,
): AbstractViewHolder<ViolationStateDataView>(itemView) {

    private var binding: SearchMpsViolationStateBinding? by viewBinding()

    override fun bind(element: ViolationStateDataView) {
        if(element.typeViolation == TypeViolation.BLACKLISTED) {
            renderViolationBlacklisted()
        } else {
            renderViolationBanned()
        }
    }

    private fun renderViolationBlacklisted(){
        renderImageViolation()
        renderTitleViolation(getString(R.string.search_mps_blacklist_response_state_title))
        renderDescViolation(getString(R.string.search_mps_blacklist_response_state_description))
        renderButtonMpsViolation()
    }

    private fun renderViolationBanned(){
        renderImageViolation()
        renderTitleViolation(getString(R.string.search_mps_banned_tobacco_state_title))
        renderDescViolation(getString(R.string.search_mps_banned_tobacco_state_description))
        renderButtonMpsViolation()
    }

    private fun renderImageViolation(){
        binding?.mpsResultViolationProductsStateImage?.loadImage(URL_IMAGE_VIOLATION_KEYWORD)
    }

    private fun renderTitleViolation(text : String) {
        binding?.mpsResultViolationProductsStateTitle?.text = text
    }

    private fun renderDescViolation(text : String) {
        binding?.mpsResultViolationProductsStateDescription?.text = text
    }

    private fun renderButtonMpsViolation() {
        binding?.buttonMPSResultViolation?.setOnClickListener {
            violationStateListener.onLearnItButtonClick()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_mps_violation_state
        private const val URL_IMAGE_VIOLATION_KEYWORD =
            "https://images.tokopedia.net/img/jbZAUJ/2022/1/14/401ff07b-464e-4898-b46e-7ee5a1552b8d.png"
    }
}
