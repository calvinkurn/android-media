package com.tokopedia.deals.pdp.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.databinding.FragmentDealsDetailLocationBinding
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.adapter.DealsDetailAllLocationAdapter
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPAllLocationViewModel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class DealsPDPAllLocationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DealsPDPAllLocationViewModel> { viewModelFactory }
    private var binding by autoClearedNullable<FragmentDealsDetailLocationBinding>()
    private var outlets: List<Outlet>? = null
    private var toolbar: HeaderUnify? = null
    private var noContentLayout: LinearLayout? = null
    private var recycleView: RecyclerView? = null
    private var searchBar: SearchBarUnify? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        outlets = arguments?.getParcelableArrayList<Outlet>(EXTRA_OUTLETS).orEmpty()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailLocationBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupHeader()
        setupSearchBar()
        observeFlowData()
        outlets?.let {
            setupRecycleView(it)
        }
    }

    private fun observeFlowData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowSearchResult.collect {
                if (it.isNullOrEmpty()) {
                    showNoContent()
                } else {
                    hideNoContent()
                    setupRecycleView(it)
                }
            }
        }
    }

    private fun setupUi() {
        view?.apply {
            toolbar = binding?.toolbar
            noContentLayout = binding?.noContent
            recycleView = binding?.recyclerView
            searchBar = binding?.searchInputView
        }
    }

    private fun setupHeader() {
        (activity as DealsPDPActivity).setSupportActionBar(toolbar)
        (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setTitle(context?.resources?.getString(com.tokopedia.deals.R.string.deals_pdp_redeem_locations))
    }

    private fun setupSearchBar() {
        searchBar?.apply {
            searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {
                    text?.let { text ->
                        outlets?.let { outlets ->
                            viewModel.submitSearch(text.toString(), outlets)
                        }
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }
    }

    private fun setupRecycleView(outlets: List<Outlet>) {
        context?.let { context ->
            val adapter = DealsDetailAllLocationAdapter(object : DealsDetailAllLocationAdapter.LocationCallBack {
                override fun onClickLocation(latLang: String) {
                    openGoogleMaps(context, latLang)
                }
            })
            recycleView?.adapter = adapter
            recycleView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            outlets.let {
                adapter.addOutlets(it)
            }

            recycleView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    KeyboardHandler.hideSoftKeyboard(activity)
                }
            })
        }
    }

    private fun openGoogleMaps(context: Context, latLng: String) {
        val gmmIntentUri = Uri.parse("${URI_MAPS}$latLng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(PACKAGE_MAPS)
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            view?.let { view ->
                Toaster.build(
                    view,
                    context.resources.getString(com.tokopedia.deals.R.string.deals_pdp_cannot_find_application),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
                ).show()
            }
        }
    }

    private fun showNoContent() {
        noContentLayout?.show()
        recycleView?.hide()
    }

    private fun hideNoContent() {
        noContentLayout?.hide()
        recycleView?.show()
    }

    companion object {
        private const val EXTRA_OUTLETS = "EXTRA_OUTLETS"
        private const val PACKAGE_MAPS = "com.google.android.apps.maps"
        private const val URI_MAPS = "geo:0,0?q="

        fun createInstance(outlets: List<Outlet>): DealsPDPAllLocationFragment {
            val fragment = DealsPDPAllLocationFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_OUTLETS, ArrayList(outlets))
            fragment.arguments = bundle
            return fragment
        }
    }
}
