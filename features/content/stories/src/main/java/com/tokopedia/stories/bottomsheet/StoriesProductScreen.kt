package com.tokopedia.stories.bottomsheet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.common.view.ContentTaggedProductBottomSheetItemView
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * @author by astidhiyaa on 25/07/23
 */

@Composable
fun ProductPage(
    products: List<ContentTaggedProductUiModel>,
    onCardClicked: (ContentTaggedProductUiModel, Int) -> Unit,
    onBuyClicked: (ContentTaggedProductUiModel, Int) -> Unit,
    onAtcClicked: (ContentTaggedProductUiModel, Int) -> Unit,
) {
    NestTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    bottom = 16.dp
                )
        ) {
            val (mvcWidgetView, loaderView, productListView) = createRefs()

            //MVC Widget TODO() : Add animated visibility
            MvcWidgetCompose(
                modifier = Modifier
                    .constrainAs(mvcWidgetView) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .height(64.dp)
            )

            //Loader TODO() : Add animated visibility
            NestLoader(modifier = Modifier
                .constrainAs(loaderView) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(mvcWidgetView.bottom)
                }
                .size(70.dp), variant = NestLoaderType.Decorative(size = NestLoaderSize.Small))


            //Product List TODO() : Add animated visibility
            LazyColumn(modifier = Modifier
                .constrainAs(productListView) {
                    height = Dimension.preferredWrapContent
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(mvcWidgetView.bottom)
                }
                .fillMaxWidth()) {
                itemsIndexed(products) { position, item ->
                    ProductCard(
                        item = item,
                        position = position,
                        onCardClicked,
                        onBuyClicked,
                        onAtcClicked
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    item: ContentTaggedProductUiModel,
    position: Int,
    onCardClicked: (ContentTaggedProductUiModel, Int) -> Unit,
    onBuyClicked: (ContentTaggedProductUiModel, Int) -> Unit,
    onAtcClicked: (ContentTaggedProductUiModel, Int) -> Unit,
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        update = {
            //TODO() please check
        },
        factory = { ctx ->
            ContentTaggedProductBottomSheetItemView(ctx, null).apply {
                bindData(item)
                setListener(object : ContentTaggedProductBottomSheetItemView.Listener {
                    override fun onProductCardClicked(
                        view: ContentTaggedProductBottomSheetItemView,
                        product: ContentTaggedProductUiModel
                    ) {
                        onCardClicked(product, position)
                    }

                    override fun onAddToCartProductButtonClicked(
                        view: ContentTaggedProductBottomSheetItemView,
                        product: ContentTaggedProductUiModel
                    ) {
                        onAtcClicked(product, position)
                    }

                    override fun onBuyProductButtonClicked(
                        view: ContentTaggedProductBottomSheetItemView,
                        product: ContentTaggedProductUiModel
                    ) {
                        onBuyClicked(product, position)
                    }
                })
            }
        }
    )
}

@Composable
fun MvcWidgetCompose(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        update = {
            //TODO() please check
        },
        factory = { ctx ->
            MvcView(ctx).apply {
               //setData()
            }
        }
    )
}
