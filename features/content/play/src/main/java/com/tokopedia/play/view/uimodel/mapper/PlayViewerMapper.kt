package com.tokopedia.play.view.uimodel.mapper

/**
 * Created by jegul on 21/01/21
 */
interface PlayViewerMapper<Input, Output> {

    fun map(input: Input): Output
}