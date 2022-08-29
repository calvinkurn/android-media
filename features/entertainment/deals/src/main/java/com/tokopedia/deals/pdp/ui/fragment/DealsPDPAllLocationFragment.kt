package com.tokopedia.deals.pdp.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.databinding.FragmentDealsDetailLocationBinding
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.adapter.DealsDetailAllLocationAdapter
import com.tokopedia.header.HeaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DealsPDPAllLocationFragment: BaseDaggerFragment() {

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
        setupRecycleView()
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

    }

    private fun setupRecycleView() {
        context?.let { context ->
            val adapter = DealsDetailAllLocationAdapter(object : DealsDetailAllLocationAdapter.LocationCallBack {
                override fun onClickLocation(latLang: String) {
                    openGoogleMaps(context, latLang)
                }
            })
            recycleView?.adapter = adapter
            recycleView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            outlets?.let {
                adapter.addOutlets(it)
            }
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

    companion object {
        private const val EXTRA_OUTLETS = "EXTRA_OUTLETS"
        private const val PACKAGE_MAPS = "com.google.android.apps.maps"
        private const val URI_MAPS = "geo:0,0?q="

        fun createInstance(outlets: List<Outlet>): DealsPDPAllLocationFragment {
            val fragment = DealsPDPAllLocationFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_OUTLETS, ArrayList(outlets))
            fragment.arguments  = bundle
            return fragment
        }
    }

}