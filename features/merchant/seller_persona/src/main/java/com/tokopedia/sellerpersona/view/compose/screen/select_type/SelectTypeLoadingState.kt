package com.tokopedia.sellerpersona.view.compose.screen.select_type

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.compose.component.NestShimmer

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PersonaTypeLoadingState() {
    val rowState = rememberLazyListState()
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp),
        state = rowState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState)
    ) {
        item { Spacer(modifier = Modifier.width(8.dp)) }
        items(count = 3) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = NestTheme.colors.NN._300,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NestShimmer(modifier = Modifier.size(80.dp), rounded = 32.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        NestShimmer(
                            modifier = Modifier
                                .width(92.dp)
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NestShimmer(
                            modifier = Modifier
                                .width(164.dp)
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NestShimmer(
                            modifier = Modifier
                                .width(116.dp)
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NestShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp),
                            rounded = 0.5.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NestShimmer(
                            modifier = Modifier
                                .width(116.dp)
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NestShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NestShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp),
                            rounded = 6.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NestShimmer(
                            modifier = Modifier
                                .width(176.dp)
                                .height(16.dp),
                            rounded = 6.dp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(all = 16.dp)
                    ) {
                        NestShimmer(modifier = Modifier.size(24.dp), rounded = 12.dp)
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.width(8.dp)) }
    }
}