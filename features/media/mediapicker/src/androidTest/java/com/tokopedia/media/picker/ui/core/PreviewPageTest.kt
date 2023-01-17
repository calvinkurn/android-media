package com.tokopedia.media.picker.ui.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.R
import com.tokopedia.media.picker.helper.utils.ImageGenerator
import com.tokopedia.media.picker.helper.utils.PickerPreviewViewActions
import com.tokopedia.media.picker.helper.utils.VideoGenerator
import com.tokopedia.media.picker.helper.utils.convertExoPlayerDuration
import com.tokopedia.media.picker.ui.PreviewTest
import com.tokopedia.media.picker.ui.widget.drawerselector.adapter.DrawerSelectionAdapter
import com.tokopedia.media.preview.ui.activity.pagers.adapter.PreviewPagerAdapter
import com.tokopedia.media.preview.ui.player.VideoControlView
import com.tokopedia.media.preview.ui.widget.pager.PreviewViewPager
import com.tokopedia.picker.common.PickerResult
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.fail


abstract class PreviewPageTest : PreviewTest() {
    object Robot {
        fun clickCloseThumbnailItem(drawerItemIndex: Int) {
            onView(ViewMatchers.withId(R.id.drawer_selector))
                .perform(PickerPreviewViewActions.clickThumbnailItemClose(drawerItemIndex))
        }

        fun clickCloseAllThumbnailItem() {
            onView(ViewMatchers.withId(R.id.drawer_selector))
                .perform(PickerPreviewViewActions.clickAllThumbnailItemClose())
        }

        fun clickThumbnailItem(thumbnailItemIndex: Int) {
            onView(ViewMatchers.withId(R.id.drawer_selector))
                .perform(PickerPreviewViewActions.clickThumbnailImage(thumbnailItemIndex))
        }

        fun clickPlayerPauseButton() {
            onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.exo_pause),
                    ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                )
            ).perform(
                ViewActions.click()
            )
        }

        fun clickPlayerPlayButton() {
            onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.exo_play),
                    ViewMatchers.isDisplayed()
                )
            ).perform(
                ViewActions.click()
            )
        }

        fun clickPreviewView() {
            onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.video_control),
                    ViewMatchers.isDisplayed()
                )
            ).perform(
                PickerPreviewViewActions.clickIn(50, 50)
            )
        }

        fun clickVideoTimeBar() {
            var duration: Long? = 0L
            onView(ViewMatchers.withId(R.id.vp_preview)).check { view, _ ->
                val viewPager = view as PreviewViewPager
                val videoPlayer =
                    viewPager.focusedChild.findViewById<VideoControlView>(R.id.video_control)

                duration = videoPlayer.player?.duration
            }

            onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.exo_progress),
                    ViewMatchers.isDisplayed()
                )
            ).perform(
                PickerPreviewViewActions.clickScrubberTimeBar(duration ?: 0L)
            )
        }

        fun waitExoPlayerInitialize() {
            Thread.sleep(1000)
        }
    }

    object Assert {
        fun assertSelectedAndPreviewIndex(drawerSelectedIndex: Int) {
            var viewPagerIndex = -1
            onView(ViewMatchers.withId(R.id.vp_preview))
                .check { view, _ ->
                    viewPagerIndex = (view as ViewPager).currentItem
                }

            assertEquals(drawerSelectedIndex, viewPagerIndex)
        }

        fun assertActivityFinish(previewTest: PreviewTest) {
            assertEquals(true, previewTest.mActivity.isFinishing)
        }

        fun assertImageListInitialization() {
            onView(ViewMatchers.withId(R.id.drawer_selector))
                .check { view, _ ->
                    val rv = view.findViewById<RecyclerView>(R.id.rv_thumbnail)
                    assertEquals(
                        ImageGenerator.IMAGES_FILES_COUNT,
                        rv.adapter?.itemCount
                    )
                }
        }

        fun assertVideoListInitialization() {
            onView(ViewMatchers.withId(R.id.drawer_selector))
                .check { view, _ ->
                    val rv = view.findViewById<RecyclerView>(R.id.rv_thumbnail)
                    assertEquals(
                        VideoGenerator.VIDEO_FILES_COUNT,
                        rv.adapter?.itemCount
                    )
                }
        }

        fun assertUploadImageResult(uploadResult: PickerResult) {
            assertEquals(
                ImageGenerator.IMAGES_FILES_COUNT,
                uploadResult.originalPaths.size
            )
        }

        fun assertUploadVideoResult(uploadResult: PickerResult) {
            assertEquals(
                VideoGenerator.VIDEO_FILES_COUNT,
                uploadResult.originalPaths.size
            )
        }

        fun assertVideoIsPlay() {
            onView(ViewMatchers.withId(R.id.vp_preview)).check { view, _ ->
                val viewPager = view as PreviewViewPager
                val isVideoPlay =
                    (viewPager.adapter as PreviewPagerAdapter).getItem(viewPager.currentItem)?.mVideoPlayer?.player()?.isPlaying

                assertEquals(true, isVideoPlay)
            }
        }

        fun assertVideoIsPause() {
            onView(ViewMatchers.withId(R.id.vp_preview)).check { view, _ ->
                val viewPager = view as PreviewViewPager
                val isVideoPlay =
                    (viewPager.adapter as PreviewPagerAdapter).getItem(viewPager.currentItem)?.mVideoPlayer?.player()?.isPlaying

                assertEquals(false, isVideoPlay)
            }
        }

        fun assertVideoControllerShow() {
            onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.nav_container),
                    ViewMatchers.isDisplayed()
                )
            ).check { view, _ ->
                assertEquals(View.VISIBLE, view.visibility)
            }
        }

        fun assertVideoCurrentDuration() {
            onView(ViewMatchers.withId(R.id.vp_preview)).check { view, _ ->
                val viewPager = view as PreviewViewPager
                val videoPlayer =
                    (viewPager.adapter as PreviewPagerAdapter).getItem(viewPager.currentItem)?.mVideoPlayer

                val targetTime = videoPlayer?.player()?.duration.convertExoPlayerDuration() - 1
                val currentTime = videoPlayer?.player()?.currentPosition.convertExoPlayerDuration()

                assertEquals(targetTime, currentTime)
            }
        }

        fun assertDeletedDrawerSelectionItem() {
            onView(ViewMatchers.withId(R.id.drawer_selector)).check { view, _ ->
                val rv = view.findViewById<RecyclerView>(R.id.rv_thumbnail)
                val rvItemCount = rv.adapter?.itemCount ?: 0

                if (rvItemCount > 0) {
                    val lastIndex = (rv.adapter?.itemCount ?: 0) - 1
                    val lastIndexVHType =
                        rv.findViewHolderForAdapterPosition(lastIndex)?.itemViewType ?: -1

                    assertEquals(DrawerSelectionAdapter.PLACEHOLDER_TYPE, lastIndexVHType)
                } else {
                    fail("RecyclerView is empty")
                }
            }
        }
    }
}
