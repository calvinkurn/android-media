package com.tokopedia.travelhomepage.homepage.presentation.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.travelhomepage.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 * @author by furqan on 04/02/2020
 */

class TravelHomepageDestinationViewDecoratorTest {

    private lateinit var viewDecorator: TravelHomepageDestinationViewDecorator

    private val recyclerView = mockk<RecyclerView>()

    @Before
    fun setup() {
        viewDecorator = TravelHomepageDestinationViewDecorator()
    }

    @Test
    fun onGetItemOffsets_FirstItem_ShouldBeZero() {
        //given
        val outRect = Rect()

        every { recyclerView.layoutManager!!.getPosition(any()) } returns 0

        //when
        viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())

        //then
        outRect.left shouldBe 0
        outRect.right shouldBe 0
    }

    @Test
    fun onGetItemOffsets_OddItem_RightOffsetShouldBe16() {
        //given
        val outRect = Rect()

        every { recyclerView.layoutManager!!.getPosition(any()) } returns 1

        //when
        viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())

        //then
        outRect.right shouldBe 16
    }

    @Test
    fun onGetItemOffsets_EvenItem_LeftOffsetShouldBe16() {
        //given
        val outRect = Rect()

        every { recyclerView.layoutManager!!.getPosition(any()) } returns 2

        //when
        viewDecorator.getItemOffsets(outRect, mockk(), recyclerView, mockk())

        //then
        outRect.left shouldBe 16
    }

}