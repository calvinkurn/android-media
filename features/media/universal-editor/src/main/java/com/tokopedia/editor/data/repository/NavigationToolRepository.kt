package com.tokopedia.editor.data.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editor.data.mapper.NavigationTypeMapper
import com.tokopedia.editor.data.mapper.NavigationTypeMapper.to
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.picker.common.MediaType
import javax.inject.Inject

interface NavigationToolRepository {

    fun tools(): List<NavigationTool>
}

class NavigationToolRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val paramFetcher: EditorParamFetcher
) : NavigationToolRepository {

    override fun tools(): List<NavigationTool> {
        val file = paramFetcher.get().firstFile
        return if (file.isVideo()) videoTools() else imageTools()
    }

    private fun imageTools(): List<NavigationTool> {
        return getToolsBy(MediaType.Image)
    }

    private fun videoTools(): List<NavigationTool> {
        return getToolsBy(MediaType.Video)
    }

    private fun getToolsBy(mediaType: MediaType): List<NavigationTool> {
        return paramFetcher.get().tools[mediaType]
            ?.map { type ->
                NavigationTypeMapper(type).to {
                    context.getString(it)
                }
            } ?: emptyList()
    }
}
