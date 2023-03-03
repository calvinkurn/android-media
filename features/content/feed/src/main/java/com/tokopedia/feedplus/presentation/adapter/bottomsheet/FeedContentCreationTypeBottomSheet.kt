package com.tokopedia.feedplus.presentation.adapter.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.feedplus.databinding.BottomSheetFeedContentCreationBinding
import com.tokopedia.feedplus.presentation.adapter.FeedCreationTypeAdapter
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedCreationTypeViewHolder
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedContentCreationTypeBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedContentCreationBinding? = null
    private val binding: BottomSheetFeedContentCreationBinding
        get() = _binding!!

    private val adapter: FeedCreationTypeAdapter by lazy {
        FeedCreationTypeAdapter(object : FeedCreationTypeViewHolder.Listener {
            override fun onClick(item: ContentCreationTypeItem) {
                dismiss()
                mListener?.onCreationItemClick(item)
            }
        })
    }

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
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetFeedContentCreationBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
        clearClose(true)
        clearHeader(true)

    }

    private fun setupView() {
        binding.rvFeedContentCreation.adapter = adapter
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    fun setData(contentAccountList: List<ContentCreationTypeItem>): FeedContentCreationTypeBottomSheet {
        adapter.updateData(contentAccountList)
        return this
    }

    companion object {
        private const val TAG = "FeedContentCreationTypeBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): FeedContentCreationTypeBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? FeedContentCreationTypeBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedContentCreationTypeBottomSheet::class.java.name
            ) as FeedContentCreationTypeBottomSheet
        }
    }

    interface Listener {
        fun onCreationItemClick(creationTypeItem: ContentCreationTypeItem)
    }
}
