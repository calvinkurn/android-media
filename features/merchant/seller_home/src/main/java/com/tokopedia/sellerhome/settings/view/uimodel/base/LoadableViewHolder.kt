package com.tokopedia.sellerhome.settings.view.uimodel.base

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.state.BaseUiModelState

abstract class LoadableViewHolder<T : Visitable<OtherSettingTypeFactory>>(itemView: View) :
        AbstractViewHolder<T>(itemView), Loadable<T>{

    inline fun <reified R : LoadableUiModel>observeUiState(element: T) {
        if (element is R) {
            when(element.uiState) {
                BaseUiModelState.Loading ->
                    renderLoadingLayout()
                BaseUiModelState.Success ->
                    renderSuccessLayout(element)
                BaseUiModelState.NoData ->
                    renderNoDataLayout()
                BaseUiModelState.Error ->
                    renderErrorLayout()
                else -> return
            }
        }
    }
    
}