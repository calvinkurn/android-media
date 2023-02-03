package com.tokopedia.wishlistcollection.view.bottomsheet

import com.tokopedia.imageassets.TokopediaImageUrl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.BottomsheetWishlistCollectionOnboardingBinding
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment

class BottomSheetOnboardingWishlistCollection: BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistCollectionOnboardingBinding>()
    private var actionListener: ActionListener? = null
    private val imageUrl = TokopediaImageUrl.imageUrl

    companion object {
        private const val TAG: String = "WishlistCollectionOnboardingBottomSheet"

        @JvmStatic
        fun newInstance(): BottomSheetOnboardingWishlistCollection {
            return BottomSheetOnboardingWishlistCollection()
        }
    }

    interface ActionListener {
        fun onClickShowCoachmarkButton()
        fun onClickSkipOnboardingButton()
    }

    fun setListener(fragment: WishlistCollectionFragment) {
        this.actionListener = fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    init {
        showCloseIcon = true
        isFullpage = false
        isKeyboardOverlap = false
    }

    private fun initLayout() {
        binding = BottomsheetWishlistCollectionOnboardingBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.onboardingState?.apply {
            setImageUrl(imageUrl)
            setTitle(getString(R.string.collection_onboarding_title))
            setDescription(getString(R.string.collection_onboarding_desc))
            setPrimaryCTAText(getString(R.string.collection_onboarding_btn_primary))
            setPrimaryCTAClickListener { actionListener?.onClickShowCoachmarkButton() }
            setSecondaryCTAText(getString(R.string.collection_onboarding_btn_secondary))
            setSecondaryCTAClickListener { actionListener?.onClickSkipOnboardingButton() }
        }
        setChild(binding?.root)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}