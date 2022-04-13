package com.tokopedia.imagepicker_insta.common.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.imagepicker_insta.common.R
import com.tokopedia.imagepicker_insta.common.databinding.BottomSheetFeedAccountTypeBinding
import com.tokopedia.imagepicker_insta.common.ui.adapter.FeedAccountTypeAdapter
import com.tokopedia.imagepicker_insta.common.ui.itemdecoration.FeedAccountTypeItemDecoration
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
import com.tokopedia.imagepicker_insta.common.ui.viewholder.FeedAccountTypeViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedAccountTypeBinding? = null
    private val binding: BottomSheetFeedAccountTypeBinding
        get() = _binding!!

    private val adapter: FeedAccountTypeAdapter by lazy {
        FeedAccountTypeAdapter(object : FeedAccountTypeViewHolder.Listener {
            override fun onClick(item: FeedAccountUiModel) {
                dismiss()
                mListener?.onAccountClick(item)
            }
        })
    }

    private val feedAccountList = mutableListOf<FeedAccountUiModel>()
    private var mListener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetFeedAccountTypeBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    private fun setupView() {
        setTitle(getString(R.string.feed_account_bottom_sheet_title))

        binding.rvFeedAccount.addItemDecoration(FeedAccountTypeItemDecoration(requireContext()))
        binding.rvFeedAccount.adapter = adapter

        adapter.updateData(feedAccountList)
    }

    fun setOnAccountClickListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(itemList: List<FeedAccountUiModel>) {
        feedAccountList.clear()
        feedAccountList.addAll(itemList)
        if(isAdded) {
            adapter.updateData(feedAccountList)
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "FeedAccountTypeBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): FeedAccountTypeBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FeedAccountTypeBottomSheet
            return if(oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    FeedAccountTypeBottomSheet::class.java.name
                ) as FeedAccountTypeBottomSheet
            }
        }
    }

    interface Listener {
        fun onAccountClick(account: FeedAccountUiModel)
    }
}