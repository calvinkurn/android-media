package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog.databinding.FragmentCatalogLoaderPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.viewmodel.CatalogLandingPageViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CatalogLandingPageFragment: BaseDaggerFragment() {

    interface CatalogLandingPageFragmentListener {
        fun onLayoutBelowVersion3()
        fun onLayoutAboveVersion4()
    }

    companion object {
        const val CATALOG_LOADER_PAGE_FRAGMENT_TAG = "CATALOG_LOADER_PAGE_FRAGMENT_TAG"
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"

        fun newInstance(catalogId: String): CatalogLandingPageFragment {
            val fragment = CatalogLandingPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: CatalogLandingPageViewModel
    private var binding by autoClearedNullable<FragmentCatalogLoaderPageBinding>()
    private var fragmentListener: CatalogLandingPageFragmentListener? = null

    override fun getScreenName() = CatalogLandingPageFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogLoaderPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePage()
        setupObservers(view)
    }

    private fun initializePage() {
        arguments?.getString(ARG_EXTRA_CATALOG_ID, "")?.let { catalogId ->
            viewModel.getProductCatalogVersion(catalogId)
        }
    }

    private fun setupObservers(view: View) {
        viewModel.usingV4AboveLayout.observe(viewLifecycleOwner) {
            if (it) {
                fragmentListener?.onLayoutAboveVersion4()
            } else {
                fragmentListener?.onLayoutBelowVersion3()
            }
        }

        viewModel.errorsToaster.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(view.context, it)
            Toaster.build(view, errorMessage, duration = Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR).show()
        }
    }

    fun setListener(listener: CatalogLandingPageFragmentListener){
        fragmentListener = listener
    }
}
