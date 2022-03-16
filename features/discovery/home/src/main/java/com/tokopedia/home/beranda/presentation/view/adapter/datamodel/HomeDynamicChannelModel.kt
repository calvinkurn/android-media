package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable
import timber.log.Timber

data class HomeDynamicChannelModel(
        val homeFlag: HomeFlag = HomeFlag(),
        var list: List<Visitable<*>> = listOf(),
        var isCache: Boolean = false,
        val isFirstPage: Boolean = false,
        var homeChooseAddressData: HomeChooseAddressData = HomeChooseAddressData(),
        var topadsPage: String = "0",
        var flowCompleted: Boolean = true,
        val isBeautyFest: Int = HomeRevampFragment.BEAUTY_FEST_NOT_SET
) {
    private var _list: MutableList<Visitable<*>> = list.toMutableList()

    init {
        evaluateChooseAddressData()
    }

    fun addWidgetModel(visitable: Visitable<*>, position: Int? = null, onListUpdated: () -> Unit = {}) {
        logChannelUpdate("Update channel: (Add widget ${visitable.javaClass.simpleName})")
        //prevent duplicate home recommendation feed data model
        if (_list.find { it is HomeRecommendationFeedDataModel } != null &&
                visitable is HomeRecommendationFeedDataModel) {
            return
        }
        if((position == null || position > _list.size)) _list.add(visitable)
        else _list.add(position, visitable)
        list = _list.copy()
        onListUpdated.invoke()
    }

    fun deleteWidgetModel(visitable: Visitable<*>? = null, position: Int, onListUpdated: () -> Unit) {
        findVisitable(
                visitable = visitable,
                position = position,
                onVisitableFoundByIndex = { pos, visitableFound ->
                    _list.remove(visitableFound)
                    list = _list.copy()
                    onListUpdated.invoke()
                },
                onVisitableFoundById = { pos, visitableFound ->
                    _list.remove(visitableFound)
                    list = _list.copy()
                    onListUpdated.invoke()
                }
        )
    }

    fun updateWidgetModel(visitable: Visitable<*>?, visitableToChange: Visitable<*>? = null, position: Int, onListUpdated: () -> Unit) {
        val visitableDest = visitableToChange ?: visitable
        findVisitable(
                visitable = visitableDest,
                position = position,
                onVisitableFoundByIndex = { pos, visitableFound ->
                    visitable?.let {
                        _list[pos] = it
                        list = _list.copy()
                        onListUpdated.invoke()
                    }
                },
                onVisitableFoundById = { pos, visitableFound ->
                    visitable?.let {
                        _list[pos] = it
                        list = _list.copy()
                        onListUpdated.invoke()
                    }
                }
        )
    }

    private fun findVisitable(
            visitable: Visitable<*>?,
            position: Int,
            onVisitableFoundByIndex: (Int, Visitable<*>) -> Unit,
            onVisitableFoundById: (Int, Visitable<*>) -> Unit
    ) {
        visitable?.let {
            //try to update by position
            if (isValidToAssignByPosition(position, visitable)) {
                logChannelUpdate("Find channel: (Find widget ${visitable.javaClass.simpleName}) by position")
                onVisitableFoundByIndex.invoke(position, visitable)
            } else {
                //else proceed by visitableId
                visitable.visitableId()?.let { visitableId ->
                    val findVisitableWithId = _list.withIndex().find { it.value.visitableId() ?: "" == visitableId }
                    findVisitableWithId?.let {
                        logChannelUpdate("Find channel: (Find widget ${visitable.javaClass.simpleName}) by visitableId")
                        onVisitableFoundById.invoke(it.index, visitable)
                    }
                }
            }
        }
    }

    private fun isValidToAssignByPosition(position: Int, visitable: Visitable<*>): Boolean {
        return (position != -1 && _list.isNotEmpty()
                && _list.size > position
                && _list[position]::class.java == visitable::class.java
                && _list[position].visitableId() == visitable.visitableId())
    }

    private fun visitableValidToAssign(position: Int, newList: MutableList<Visitable<*>>, visitable: Visitable<*>) =
            position != -1 && newList.isNotEmpty() && newList.size > position && newList[position]::class.java == visitable::class.java

    fun evaluateChooseAddressData() {
        val processList = _list.copy()
        val homeHeaderOvoDataModel = processList.find { visitable -> visitable is HomeHeaderDataModel }
        val headerIndex = processList.indexOfFirst { visitable -> visitable is HomeHeaderDataModel }
        (homeHeaderOvoDataModel as? HomeHeaderDataModel)?.let {
            it.needToShowChooseAddress = homeChooseAddressData.isActive
            _list[headerIndex] = homeHeaderOvoDataModel
        }
    }

    fun initRecom(onNeedTabLoad: () -> Unit) {
        val mutableIterator = _list.iterator()
        for (e in mutableIterator) {
            if (e is HomeRetryModel) {
                mutableIterator.remove()
                break
            }
        }
        if (!isCache) {
            onNeedTabLoad.invoke()
        }
    }

    fun evaluateRecommendationSection(currentHomeRecom: HomeRecommendationFeedDataModel?): Boolean {
        if(_list.find { it::class.java == HomeLoadingMoreModel::class.java } != null)
            return false

        //reuse the recommendation viewmodel
        var detectHomeRecom = _list.find { visitable -> visitable is HomeRecommendationFeedDataModel }
        if (detectHomeRecom == null) {
            detectHomeRecom = currentHomeRecom
        }
        if (detectHomeRecom != null) {
            val isAddressChanged =
                    (detectHomeRecom as? HomeRecommendationFeedDataModel)?.homeChooseAddressData != homeChooseAddressData
            if (isAddressChanged) {
                return true
            }
        } else {
            return true
        }
        currentHomeRecom?.let { addWidgetModel(it) {} }
        return false
    }

    private fun copyWidget(
            homeDynamicChannelModel: HomeDynamicChannelModel,
            validation: (Visitable<*>) -> Boolean,
            modification: (Visitable<*>?) -> Visitable<*>? = { null }
    ) {
        val listToCopy = homeDynamicChannelModel.list
        val widget = listToCopy.find { visitable -> validation.invoke(visitable) }
        val processedWidget =
            if (modification.invoke(widget) == null) widget else modification.invoke(widget)
        if(processedWidget != null) {
            val widgetIndex = _list.indexOfFirst { visitable -> validation.invoke(visitable) }
            if(widgetIndex != -1){
                _list[widgetIndex] = processedWidget
            } else {
                addWidgetModel(processedWidget)
            }
        }
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }

    fun Visitable<*>.visitableId(): String? {
        return when (this) {
            is HomeVisitable -> this.visitableId()
            is HomeComponentVisitable -> this.visitableId()
            is RecommendationVisitable -> this.visitableId()
            else -> null
        }
    }

    fun setAndEvaluateHomeChooseAddressData(homeChooseAddressData: HomeChooseAddressData) {
        this.homeChooseAddressData = homeChooseAddressData
        evaluateChooseAddressData()
    }
}