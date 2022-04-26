package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.createpost.createpost.databinding.FragmentLastTaggedProductBinding
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class LastTaggedProductFragment @Inject constructor(
    private val userSession: UserSessionInterface,
): TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "LastTaggedProductFragment"

    private var _binding: FragmentLastTaggedProductBinding? = null
    private val binding: FragmentLastTaggedProductBinding
        get() = _binding!!

    private lateinit var viewModelFactoryCreator: ViewModelProvider.Factory
    private lateinit var viewModel: ProductTagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireParentFragment(),
            viewModelFactoryCreator
        )[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLastTaggedProductBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setViewModelFactory(viewModelFactoryCreator: ViewModelProvider.Factory) {
        this.viewModelFactoryCreator = viewModelFactoryCreator
    }

    companion object {
        const val TAG = "FeedAccountTypeBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): LastTaggedProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? LastTaggedProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                LastTaggedProductFragment::class.java.name
            ) as LastTaggedProductFragment
        }
    }
}