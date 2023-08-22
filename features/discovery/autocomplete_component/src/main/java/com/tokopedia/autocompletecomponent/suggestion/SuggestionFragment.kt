package com.tokopedia.autocompletecomponent.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.searchbar.SearchBarViewModel
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipListener
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocompletecomponent.util.HasViewModelFactory
import com.tokopedia.autocompletecomponent.util.OnScrollListenerAutocomplete
import com.tokopedia.autocompletecomponent.util.SCREEN_UNIVERSEARCH
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class SuggestionFragment :
    TkpdBaseV4Fragment(),
    SuggestionContract.View,
    SuggestionListener,
    SuggestionTopShopListener,
    SuggestionChipListener {

    companion object {
        const val SUGGESTION_FRAGMENT_TAG = "SUGGESTION_FRAGMENT_TAG"
        private const val SEARCH_PARAMETER = "SEARCH_PARAMETER"
        private const val ACTIVE_KEYWORD_VALUE = "ACTIVE_KEYWORD_VALUE"
        private const val ACTIVE_KEYWORD_POSITION = "ACTIVE_KEYWORD_POSITION"
        private const val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

        @JvmStatic
        fun create(
            component: SuggestionComponent,
        ) = SuggestionFragment().apply {
            component.inject(this)
        }
    }

    var presenter: SuggestionContract.Presenter? = null
        @Inject set

    var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null
        @Inject set

    var suggestionTracking: SuggestionTracking? = null
        @Inject set

    private val viewModel: SearchBarViewModel? by lazy {
        val activity = activity ?: return@lazy null
        if (activity !is HasViewModelFactory) return@lazy null
        val factory = activity.viewModelFactory ?: return@lazy null
        ViewModelProvider(activity, factory).get()
    }

    override val className: String
        get() = activity?.javaClass?.name ?: ""

    private fun getReimagineVariant(): Search1InstAuto = presenter?.getReimagineVariant() ?: Search1InstAuto.CONTROL

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val suggestionTypeFactory = SuggestionAdapterTypeFactory(
        suggestionListener = this,
        suggestionTopShopListener = this,
        suggestionChipListener = this,
        getReimagineVariant()
    )
    private val suggestionAdapter = SuggestionAdapter(suggestionTypeFactory)

    private val recyclerViewSuggestion by lazy {
        view?.findViewById<RecyclerView?>(R.id.recyclerViewSuggestion)
    }

    private val tgExceedKeywordLimit by lazy {
        view?.findViewById<Typography>(R.id.tg_suggestion_exceed_keyword_limit)
    }

    private var coachMark: CoachMark2? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareView()
        presenter?.attachView(this)
    }

    private fun prepareView() {
        recyclerViewSuggestion?.apply {
            adapter = suggestionAdapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            addOnScrollListener(OnScrollListenerAutocomplete(activity))
        }
    }

    override fun onDestroyView() {
        presenter?.let {
            it.markSuggestionCoachMark()
            it.detachView()
        }
        super.onDestroyView()
    }

    override fun getScreenName(): String {
        return SCREEN_UNIVERSEARCH
    }

    override fun showSuggestionResult(list: List<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        recyclerViewSuggestion?.show()
        suggestionAdapter.replaceData(list)

        suggestionViewUpdateListener?.showSuggestionView()
    }

    override fun hideSuggestionResult() {
        recyclerViewSuggestion?.hide()
    }

    override fun showExceedKeywordLimit() {
        stopTracePerformanceMonitoring()
        tgExceedKeywordLimit?.show()
        suggestionViewUpdateListener?.showSuggestionView()
    }

    override fun hideExceedKeywordLimit() {
        tgExceedKeywordLimit?.hide()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
        performanceMonitoring = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState == null) return

        val searchParameter = savedInstanceState.getSerializable(SEARCH_PARAMETER)
            as HashMap<String, String>
        val activeKeyword = SearchBarKeyword(
            savedInstanceState.getInt(ACTIVE_KEYWORD_POSITION),
            savedInstanceState.getString(ACTIVE_KEYWORD_VALUE, "")
        )

        presenter?.getSuggestion(searchParameter, activeKeyword)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val presenter = presenter ?: return

        outState.putSerializable(
            SEARCH_PARAMETER,
            HashMap<String, Any>(presenter.getSearchParameter())
        )
        val activeKeyword = presenter.getActiveKeyword()
        outState.putInt(ACTIVE_KEYWORD_POSITION, activeKeyword.position)
        outState.putString(ACTIVE_KEYWORD_VALUE, activeKeyword.keyword)
    }

    fun getSuggestion(searchParameter: Map<String, String>, activeKeyword: SearchBarKeyword) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)

        presenter?.getSuggestion(searchParameter, activeKeyword)
    }

    fun hideSuggestionCoachMark() {
        coachMark?.dismiss()
    }

    fun setIsTyping(isTyping: Boolean) {
        presenter?.setIsTyping(isTyping)
    }

    override fun onItemClicked(item: BaseSuggestionDataView) {
        presenter?.onSuggestionItemClicked(item)
    }

    override fun onChipImpressed(item: BaseSuggestionDataView.ChildItem) {
        suggestionTracking?.eventImpressionSuggestion(item)
    }

    override fun onChipClicked(
        baseSuggestionDataView: BaseSuggestionDataView,
        childItem: BaseSuggestionDataView.ChildItem
    ) {
        presenter?.onSuggestionChipClicked(baseSuggestionDataView, childItem)
    }

    override fun onItemImpressed(item: BaseSuggestionDataView) {
        presenter?.onSuggestionItemImpressed(item)
    }

    override fun dropKeyBoard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    override fun route(
        applink: String,
        searchParameter: Map<String, String>,
        activeKeyword: SearchBarKeyword,
    ) {
        activity?.let {
            val modifiedApplink = getModifiedApplink(applink, searchParameter, activeKeyword)
            RouteManager.route(it, modifiedApplink)
        }
    }

    override fun applySuggestionToSelectedKeyword(
        suggestedText: String,
        activeKeyword: SearchBarKeyword,
    ) {
        viewModel?.onApplySuggestionToSelectedKeyword(suggestedText, activeKeyword)
    }

    override fun finish() {
        activity?.finish()
    }

    override fun copyTextToSearchView(text: String) {
        suggestionViewUpdateListener?.setSearchQuery("$text ")
    }

    override fun trackEventClickKeyword(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickKeyword(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun trackEventClickCurated(
        eventLabel: String,
        campaignCode: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickCurated(
            eventLabel,
            campaignCode,
            dimension90,
            baseSuggestionDataView
        )
    }

    override fun trackEventClickShop(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickShop(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun trackEventClickProfile(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickProfile(eventLabel, baseSuggestionDataView)
    }

    override fun trackEventClickRecentKeyword(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickRecentKeyword(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun onTopShopCardClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter?.onTopShopCardClicked(topShopData)
    }

    override fun onTopShopSeeMoreClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter?.onTopShopCardClicked(topShopData)
    }

    override fun trackEventClickTopShopCard(eventLabel: String) {
        suggestionTracking?.eventClickTopShop(eventLabel)
    }

    override fun trackEventClickTopShopSeeMore(eventLabel: String) {
        suggestionTracking?.eventClickTopShopSeeMore(eventLabel)
    }

    override fun trackEventClickLocalKeyword(
        eventLabel: String,
        userId: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickLocalKeyword(
            eventLabel,
            userId,
            dimension90,
            baseSuggestionDataView,
        )
    }

    override fun trackEventClickGlobalKeyword(
        eventLabel: String,
        userId: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickGlobalKeyword(
            eventLabel,
            userId,
            dimension90,
            baseSuggestionDataView,
        )
    }

    override fun trackEventClickProductLine(item: BaseSuggestionDataView, eventLabel: String, userId: String) {
        val productDataLayer = item.getProductLineAsObjectDataLayer()
        suggestionTracking?.eventClickSuggestionProductLine(productDataLayer, eventLabel, userId)
    }

    override val chooseAddressData: LocalCacheModel
        get() = context?.let {
            try {
                ChooseAddressUtils.getLocalizingAddressData(it)
            } catch (e: Throwable) {
                ChooseAddressConstant.emptyAddress
            }
        } ?: ChooseAddressConstant.emptyAddress

    override fun trackTokoNowEventClickCurated(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickTokoNowCurated(eventLabel, baseSuggestionDataView)
    }

    override fun trackTokoNowEventClickKeyword(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickTokoNowKeyword(eventLabel, baseSuggestionDataView)
    }

    override fun trackClickChip(
        eventLabel: String,
        dimension90: String,
        childItem: BaseSuggestionDataView.ChildItem,
    ) {
        suggestionTracking?.eventClickChipSuggestion(childItem)
    }

    override fun trackEventImpressCurated(
        label: String,
        campaignCode: String,
        pageSource: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventImpressCurated(
            label,
            campaignCode,
            pageSource,
            baseSuggestionDataView,
        )
    }

    override fun trackEventImpression(item: BaseSuggestionDataView) {
        suggestionTracking?.eventImpressionSuggestion(item)
    }

    override fun trackEventClick(item: BaseSuggestionDataView) {
        suggestionTracking?.eventClickSuggestion(item)
    }

    override fun showSuggestionCoachMark() {
        val lastKeywordItemPosition = suggestionAdapter.getLastItem {
            it is SuggestionSingleLineDataDataView && it.data.type == TYPE_KEYWORD
        }
        val view = recyclerViewSuggestion?.layoutManager?.getChildAt(lastKeywordItemPosition) ?: return

        if (coachMark != null) return

        buildCoachMark2 {
            presenter?.markSuggestionCoachMark()
        }

        val coachMarkList = createSuggestionCoachMarkList(view)

        coachMark?.showCoachMark(coachMarkList, null, 0)
    }
    private fun buildCoachMark2(action: () -> Unit) {
        val activity = activity ?: return
        coachMark = CoachMark2(activity).apply {
            setOnDismissListener {
                coachMark = null
            }
            onDismissListener = {
                action()
            }
        }
    }
    private fun createSuggestionCoachMarkList(
        view: View
    ) : ArrayList<CoachMark2Item> {
        return arrayListOf(
            createSuggestionCoachMark(view),
            createSuggestionShortCutCoachMark(view),
        )
    }
    private fun createSuggestionCoachMark(view: View): CoachMark2Item {
        val suggestionView: View = view.findViewById(R.id.singleLineTitle)
        return CoachMark2Item(
            suggestionView,
            getString(R.string.suggestion_keyword_coach_mark_title),
            getString(R.string.suggestion_keyword_coach_mark_message),
            CoachMark2.POSITION_BOTTOM
        )
    }

    private fun createSuggestionShortCutCoachMark(view: View): CoachMark2Item {
        val suggestionShortCutView: View = view.findViewById(R.id.actionShortcutButton)
        return CoachMark2Item(
            suggestionShortCutView,
            getString(R.string.suggestion_keyword_arrow_coach_mark_title),
            getString(R.string.suggestion_keyword_arrow_coach_mark_message),
            CoachMark2.POSITION_BOTTOM
        )
    }

    interface SuggestionViewUpdateListener {

        fun showSuggestionView()
        fun setSearchQuery(keyword: String)
    }
}
