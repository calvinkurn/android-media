package com.tokopedia.shop.pageheader.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.shop.databinding.BottomSheetShopContentCreationOptionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on December 01, 2022
 */
class ShopContentCreationOptionBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null

    private var _binding: BottomSheetShopContentCreationOptionBinding? = null
    private val binding: BottomSheetShopContentCreationOptionBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun setupBottomSheet(inflater: LayoutInflater) {
        _binding = BottomSheetShopContentCreationOptionBinding.inflate(inflater)
        setChild(binding.root)

        clearContentPadding = true
    }

    private fun setupView() {
        /**
         * Need to set SHORT_VIDEO programatically for now
         * because SHORT_VIDEO hasn't registered in IconUnify values.xml yet
         */
        binding.icShorts.setImage(IconUnify.SHORT_VIDEO)
    }

    private fun setupListener() {
        binding.llShorts.setOnClickListener {
            mListener?.onShortsCreationClicked()
            dismiss()
        }

        binding.llBroadcaster.setOnClickListener {
            mListener?.onBroadcastCreationClicked()
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    companion object {
        private const val TAG = "ShopContentCreationOptionBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ShopContentCreationOptionBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ShopContentCreationOptionBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShopContentCreationOptionBottomSheet::class.java.name
            ) as ShopContentCreationOptionBottomSheet
        }
    }

    interface Listener {
        fun onBroadcastCreationClicked()

        fun onShortsCreationClicked()
    }

}
