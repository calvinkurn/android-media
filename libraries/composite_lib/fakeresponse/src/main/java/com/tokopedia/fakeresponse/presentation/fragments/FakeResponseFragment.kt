package com.tokopedia.fakeresponse.presentation.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.fakeresponse.Preference
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.Router
import com.tokopedia.fakeresponse.SortBy
import com.tokopedia.fakeresponse.data.diProvider.fragments.FakeResponseFragmentProvider
import com.tokopedia.fakeresponse.presentation.activities.BaseActivity
import com.tokopedia.fakeresponse.presentation.activities.FakeResponseActivity
import com.tokopedia.fakeresponse.presentation.adapters.PagerAdapter
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.FakeResponseVM


class FakeResponseFragment : BaseFragment() {

    companion object {
        fun newInstance() = FakeResponseFragment()
    }

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var toolbar: Toolbar

    private lateinit var pagerAdapter: PagerAdapter
    lateinit var viewModel: FakeResponseVM


    override fun getLayout() = R.layout.fake_response_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initVars() {
        pagerAdapter = PagerAdapter(childFragmentManager)
    }

    override fun initViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.visibility = View.GONE
    }

    override fun setupFragment() {
        inijectComponents()
        setListeners()
        viewPager.adapter = pagerAdapter

        (activity as BaseActivity).setSupportActionBar(toolbar)
    }

    fun setListeners() {

        viewModel.clearSqlRecords.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    handleAllRecordsDeleted()
                }
                else -> {
                    // no-op
                }
            }
        })

        viewModel.resetData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    handleResetData()
                }
                else -> {
                    // no-op
                }
            }
        })
    }

    fun handleAllRecordsDeleted() {
        if (context is FakeResponseActivity) {
            pagerAdapter.fragmentList.forEach {
                if (it is HomeFragment) {
                    it.showEmptyUi()
                }
            }

        }
    }

    fun handleResetData() {
        if (context is FakeResponseActivity) {
            Toast.makeText(
                context,
                "All data cleared, Restart app to use this library again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun inijectComponents() {
        FakeResponseFragmentProvider().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.gql_settings_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gql_menu_clear_records -> {
                viewModel.deleteAllGqlRecords()
            }
            R.id.gql_menu_reset -> {
                viewModel.resetLibrary()
            }
            R.id.gql_add_gql_record -> {
                Router.routeToAddGql(context)
            }
            R.id.gql_add_rest_record -> {
                Router.routeToAddRest(context)
            }
            R.id.gql_menu_search -> {
                Router.routeToSearch(context)
            }
            R.id.gql_menu_paste_text -> {
                Router.routeToPasteTextActivity(activity)
            }
            R.id.gql_sort_time_asc -> {
                Preference.updateSortBy(SortBy.TIME_DESC)
                pagerAdapter.fragmentList.firstOrNull()?.onResume()
            }
            R.id.gql_sort_default -> {
                Preference.updateSortBy(SortBy.DEFAULT)
                pagerAdapter.fragmentList.firstOrNull()?.onResume()
            }
            R.id.notification_on -> {
                Preference.updateNotification(true)
                Toast.makeText(context,
                    context?.getString(com.tokopedia.fakeresponse.R.string.notification_fake_response_on)
                        .orEmpty(),
                    Toast.LENGTH_LONG
                ).show()
            }
            R.id.notification_off -> {
                Preference.updateNotification(false)
                Toast.makeText(context,
                    context?.getString(com.tokopedia.fakeresponse.R.string.notification_fake_response_off)
                        .orEmpty(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return true
    }
}
