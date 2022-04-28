package com.tokopedia.createpost.producttag.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.createpost.createpost.databinding.FragmentMyShopProductBinding
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class MyShopProductFragment : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "MyShopProductFragment"

    private var _binding: FragmentMyShopProductBinding? = null
    private val binding: FragmentMyShopProductBinding
        get() = _binding!!

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var viewModel: ProductTagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyShopProductBinding.inflate(
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

    fun setViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    companion object {
        const val TAG = "MyShopProductFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): MyShopProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? MyShopProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                MyShopProductFragment::class.java.name
            ) as MyShopProductFragment
        }
    }
}