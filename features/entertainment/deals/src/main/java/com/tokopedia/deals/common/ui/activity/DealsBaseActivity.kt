package com.tokopedia.deals.common.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.R
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.common.listener.SearchBarActionListener
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.ui.customview.SelectLocationBottomSheet
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.activity_base_deals.*
import kotlinx.android.synthetic.main.content_base_deals_search_bar.*
import kotlinx.android.synthetic.main.content_base_toolbar.*
import javax.inject.Inject
import kotlin.math.abs


/**
 * @author by jessica on 11/06/20
 */

abstract class DealsBaseActivity : BaseSimpleActivity(), CurrentLocationCallback {

    private lateinit var dealsComponent: DealsComponent
    private var permissionCheckerHelper = PermissionCheckerHelper()

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var dealsAnalytics: DealsAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var baseViewModel: DealsBaseViewModel

    protected var isLandmarkPage = false

    var searchBarActionListener: SearchBarActionListener? = null

    private lateinit var locationBottomSheet: SelectLocationBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initViewModel()
        setupView()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initInjector()
        GraphqlClient.init(this)
    }

    private fun initInjector() {
        getDealsComponent().inject(this)
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        baseViewModel = viewModelProvider.get(DealsBaseViewModel::class.java)
    }

    open fun isHomePage(): Boolean = false

    private fun setupView() {
        setUpScrollView()
        handleToolbarVisibilityWihLocName(currentLoc.name)
        setupLocation()
        setupSearchBar()
        setupBackButton()
        setupOrderListMenu()
    }

    private fun setUpScrollView() {

        appBarLayoutSearchContent?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange >= -searchBarDealsBaseSearch.height) {
                //collapse
                imgDealsSearchIcon.show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayoutSearchContent.elevation = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                }
            } else {
                imgDealsSearchIcon.hide()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayoutSearchContent.elevation = resources.getDimension(R.dimen.deals_dp_0)
                }
            }
        })

        imgDealsSearchIcon.setOnClickListener {
            searchBarActionListener?.onClickSearchBar()
        }
    }

    private fun setupLocation() {
        if (isHomePage()) {
            setUpPermissionChecker()
        } else {
            baseViewModel.setCurrentLocation(dealsLocationUtils.getLocation())
        }
        observeLocation()

        currentLoc = dealsLocationUtils.getLocation()
        txtDealsBaseLocationHint.setOnClickListener { onClickLocation() }
        txtDealsBaseLocationTitle.setOnClickListener { onClickLocation() }
    }

    private fun observeLocation() {
        baseViewModel.observableCurrentLocation.observe(this, Observer {
            handleToolbarVisibilityWihLocName(it.name)
            dealsLocationUtils.updateLocation(it)
        })
    }

    fun renderLocationName(name: String) {
        txtDealsBaseLocationTitle.text = name
    }

    private fun setUpPermissionChecker() {
        permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onNeverAskAgain(permissionText: String) {}

                    override fun onPermissionDenied(permissionText: String) {
                        val defaultLocation = dealsLocationUtils.updateLocationToDefault()
                        baseViewModel.setCurrentLocation(defaultLocation)
                    }

                    override fun onPermissionGranted() {
                        if (isHomePage()) {
                            dealsLocationUtils.detectAndSendLocation(this@DealsBaseActivity,
                                    permissionCheckerHelper, this@DealsBaseActivity)
                        } else {
                            handleToolbarVisibilityWihLocName(dealsLocationUtils.getLocation().name)
                        }
                    }

                })
    }

    override fun setCurrentLocation(location: Location) {
        if (location.name.isEmpty()) {
            baseViewModel.getCurrentLocation(location.coordinates)
        } else {
            baseViewModel.setCurrentLocation(location)
        }
    }

    override fun setChangedLocation() { /* do nothing */
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(this,
                    requestCode, permissions, grantResults)
        }
    }

    private fun setupOrderListMenu() {
        imgDealsOrderListMenu.setOnClickListener {
            dealsAnalytics.clickOrderListDeals()
            RouteManager.route(this, ApplinkConst.DEALS_ORDER)
        }
    }

    private fun setupSearchBar() {
        with(searchBarDealsBaseSearch) {
            if (!isSearchAble()) {
                searchBarTextField.inputType = InputType.TYPE_NULL
                searchBarTextField.setOnClickListener { searchBarActionListener?.onClickSearchBar() }
            } else {
                searchBarTextField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        searchBarActionListener?.afterSearchBarTextChanged(s.toString())
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* do nothing */
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* do nothing */
                    }
                })
            }
        }

    }

    private fun setupBackButton() {
        imgDealsBaseBackIcon.setOnClickListener { this.onBackPressed() }
    }

    protected fun getDealsComponent(): DealsComponent {
        if (!::dealsComponent.isInitialized) {
            dealsComponent = DealsComponentInstance.getDealsComponent(application, this)
        }
        return dealsComponent
    }

    var currentLoc = Location()

    private fun onClickLocation() {
        locationBottomSheet = SelectLocationBottomSheet(currentLoc, isLandmarkPage, object : CurrentLocationCallback {
            override fun setCurrentLocation(location: Location) {
                locationBottomSheet.dismiss()
                this@DealsBaseActivity.setCurrentLocation(location)
            }

            override fun setChangedLocation() {
                locationBottomSheet.dismiss()
                this@DealsBaseActivity.setCurrentLocation(dealsLocationUtils.getLocation())
            }
        })
        locationBottomSheet.show(supportFragmentManager, "")
    }

    fun changeLocationBasedOnCache(): Boolean {
        val location = dealsLocationUtils.getLocation()
        if (location != currentLoc) {
            currentLoc = location
            renderLocationName(location.name)
            return true
        }
        return false
    }

    /**
     *  to determined if the search bar is type-able or just clickable.
     *  @return Boolean = true if it is type-able
     *          Boolean = false if it is only clickable
     **/
    open fun isSearchAble(): Boolean = true
    override fun getLayoutRes(): Int = R.layout.activity_base_deals
    override fun getParentViewResourceID(): Int = R.id.containerBaseDealsParentView

    fun handleToolbarVisibilityWihLocName(name: String) {
        if (!name.isBlank()) {
            txtDealsBaseLocationTitle.text = name
            txtDealsBaseLocationTitle.show()
            searchBarDealsBaseSearch.show()
            shimmerSearchBar.hide()
            shimmerDealsBaseLocationTitle.hide()
        } else {
            shimmerSearchBar.show()
            shimmerDealsBaseLocationTitle.show()
        }
    }
}