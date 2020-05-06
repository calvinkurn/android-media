package com.tokopedia.exploreCategory.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.R
import com.tokopedia.exploreCategory.adapter.ECServiceAdapter
import com.tokopedia.exploreCategory.adapter.ECServiceAdapterFactory
import com.tokopedia.exploreCategory.di.DaggerECComponent
import com.tokopedia.exploreCategory.di.ECComponent
import com.tokopedia.exploreCategory.ui.viewholder.ECAccordionViewHolder
import com.tokopedia.exploreCategory.viewmodel.ECServiceViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.ec_service_fragment_layout.*
import java.util.*
import javax.inject.Inject

class ECServiceFragment : BaseViewModelFragment<ECServiceViewModel>(), ECAccordionViewHolder.AccordionListener {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var ecServiceViewModel: ECServiceViewModel
    private val adapter: ECServiceAdapter = ECServiceAdapter(ECServiceAdapterFactory(this))

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        handleBundle()
    }

    private fun handleBundle() {
        if (arguments?.containsKey(EXTRA_CATEGORY_ID) == true) {
            ecServiceViewModel.currentActiveCategory = arguments?.getInt(EXTRA_CATEGORY_ID, 1)?.minus(1) ?: 0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        ec_service_recycler_view.layoutManager = layoutManager
        ec_service_recycler_view.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ec_service_fragment_layout, container, false)
    }

    private fun setObservers() {
        ecServiceViewModel.getShimmerVisibility().observe(this, Observer<Boolean> { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.startShimmer()
                else
                    adapter.stopShimmer()
            }
        })

        ecServiceViewModel.getErrorMessage().observe(this, Observer<String> { message ->
            if (!TextUtils.isEmpty(message)) {
                try {
                    activity?.findViewById<View>(android.R.id.content)?.let {
                        Toaster.make(it, message, Snackbar.LENGTH_LONG)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        ecServiceViewModel.categories.observe(this, Observer {
            adapter.addMoreData(it)
        })

        ecServiceViewModel.notifyAdapter.observe(this, Observer {
            adapter.notifyItemChanged(it)
        })
    }

    override fun onAccordionClick(categoryIndex: Int) {
        ecServiceViewModel.onAccordionClicked(categoryIndex)
    }

    override fun getViewModelType(): Class<ECServiceViewModel> {
        return ECServiceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        ecServiceViewModel = viewModel as ECServiceViewModel
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): ECComponent =
            DaggerECComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    companion object {

        private const val EXTRA_CATEGORY_ID = "CATEGORY_ID"

        val fragmentInstance: Fragment
            get() = ECServiceFragment()

        fun getFragmentInstance(categoryId: Int): Fragment {
            val fragment = ECServiceFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId)
            fragment.arguments = bundle

            return fragment
        }
    }
}