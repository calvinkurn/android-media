package com.tokopedia.people.views.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.people.databinding.UpBottomSheetOptionBinding
import com.tokopedia.people.R
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.adapter.Option
import com.tokopedia.people.views.adapter.UserProfileOptionAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 28/11/22
 */
class UserProfileOptionBottomSheet : BottomSheetUnify() {

    private var _binding: UpBottomSheetOptionBinding? = null
    private val binding: UpBottomSheetOptionBinding
        get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        UserProfileOptionAdapter()
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

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupBottomSheet() {
        _binding = UpBottomSheetOptionBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.adapter = adapter

        adapter.submitList(getOptions())
    }

    private fun getOptions(): List<Option> {
        return listOf(
            Option(
                getString(R.string.up_block_user),
                MethodChecker.getColor(
                    requireContext(),
                    unifyR.color.Unify_NN950,
                ),
            ) { mListener?.onBlockingUser(this) }
        )
    }

    companion object {
        private const val TAG = "UserProfileOptionBottomSheet"

        fun get(
            fragmentManager: FragmentManager,
        ): UserProfileOptionBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? UserProfileOptionBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): UserProfileOptionBottomSheet {
            return get(fragmentManager) ?: run {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    UserProfileOptionBottomSheet::class.java.name
                ) as UserProfileOptionBottomSheet
            }
        }
    }

    interface Listener {
        fun onBlockingUser(bottomSheet: UserProfileOptionBottomSheet)
    }
}
