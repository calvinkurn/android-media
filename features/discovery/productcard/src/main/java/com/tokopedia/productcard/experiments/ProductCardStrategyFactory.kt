package com.tokopedia.productcard.experiments

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import com.tokopedia.productcard.R
import com.tokopedia.productcard.experiments.ProductCardStrategyFactory.ReimagineTemplate.GRID
import com.tokopedia.productcard.experiments.ProductCardStrategyFactory.ReimagineTemplate.LIST

internal object ProductCardStrategyFactory {

    fun gridStrategy(productCardView: ViewGroup, attrs: AttributeSet? = null): ProductCardStrategy {
        attrs ?: return GridViewStrategy(productCardView)

        val typedArray = productCardView.context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return GridViewStrategy(productCardView)

        return try {
            tryCreateGridStrategy(typedArray, productCardView)
        } catch (_: Throwable) {
            GridViewStrategy(productCardView)
        } finally {
            typedArray.recycle()
        }
    }

    private fun tryCreateGridStrategy(typedArray: TypedArray, productCardView: ViewGroup) =
        if (isReimagine(typedArray))
            ReimagineTemplate
                .of(reimagineTemplate(typedArray, GRID))
                .create(productCardView)
        else GridViewStrategy(productCardView)

    private fun isReimagine(typedArray: TypedArray) =
        typedArray.getBoolean(R.styleable.ProductCardView_reimagine, false)
            && ProductCardExperiment.isReimagine()

    private fun reimagineTemplate(typedArray: TypedArray, defaultTemplate: ReimagineTemplate) =
        typedArray.getInt(R.styleable.ProductCardView_reimagine_template, defaultTemplate.value)

    fun listStrategy(productCardView: ViewGroup, attrs: AttributeSet? = null): ProductCardStrategy {
        attrs ?: return ListViewStrategy(productCardView)

        val typedArray = productCardView.context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return ListViewStrategy(productCardView)

        return try {
            tryCreateListStrategy(typedArray, productCardView)
        } catch (_: Throwable) {
            ListViewStrategy(productCardView)
        } finally {
            typedArray.recycle()
        }
    }

    private fun tryCreateListStrategy(typedArray: TypedArray, productCardView: ViewGroup) =
        if (isReimagine(typedArray))
            ReimagineTemplate
                .of(reimagineTemplate(typedArray, LIST))
                .create(productCardView)
        else ListViewStrategy(productCardView)

    private enum class ReimagineTemplate(val value: Int) {
        GRID(1) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy =
                ReimagineGridViewStrategy(productCardView)
        },

        GRID_CAROUSEL(2) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy =
                ReimagineGridCarouselViewStrategy(productCardView)
        },

        LIST(3) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy =
                ReimagineListViewStrategy(productCardView)
        },

        LIST_CAROUSEL(4) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy =
                ReimagineListCarouselViewStrategy(productCardView)
        };

        abstract fun create(productCardView: ViewGroup): ProductCardStrategy

        companion object {
            fun of(value: Int): ReimagineTemplate =
                ReimagineTemplate.values().find { it.value == value } ?: GRID
        }
    }
}
