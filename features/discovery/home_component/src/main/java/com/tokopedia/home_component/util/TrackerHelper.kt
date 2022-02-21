package com.tokopedia.home_component.util

import com.tokopedia.home_component.model.ChannelGrid

/**
 * Created by yfsx on 23/11/21.
 */

private const val TOPADS = "topads"
private const val NONTOPADS = "non topads"
fun ChannelGrid.getTopadsString(): String {
    return if (this.isTopads) TOPADS else NONTOPADS
}