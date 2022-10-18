package com.tokopedia.deals.checkout.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity
import com.tokopedia.deals.databinding.FragmentDealsCheckoutLocationBinding
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.ui.adapter.DealsDetailAllLocationAdapter
import com.tokopedia.header.HeaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DealsCheckoutLocationsFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentDealsCheckoutLocationBinding>()
    private var outlets: List<Outlet>? = null
    private var recycleView: RecyclerView? = null
    private var toolbar: HeaderUnify? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsCheckoutComponent::class.java).inject(this)
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
        binding = FragmentDealsCheckoutLocationBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupHeader()
        outlets?.let {
            setupRecycleView(it)
        }
    }

    private fun setupUi() {
        view?.apply {
            toolbar = binding?.toolbar
            recycleView = binding?.recyclerView
        }
    }

    private fun setupHeader() {
        toolbar?.headerTitle = context?.resources?.getString(com.tokopedia.deals.R.string.deals_pdp_redeem_locations).orEmpty()
        (activity as DealsCheckoutActivity).setSupportActionBar(toolbar)
        (activity as DealsCheckoutActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            adapter.addOutlets(outlets)
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

        fun createInstance(outlets: List<Outlet>): DealsCheckoutLocationsFragment {
            val fragment = DealsCheckoutLocationsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_OUTLETS, ArrayList(outlets))
            fragment.arguments = bundle
            return fragment
        }
    }
}
