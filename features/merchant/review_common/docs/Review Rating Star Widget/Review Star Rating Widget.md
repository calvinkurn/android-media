---
title: "Review Star Rating Widget"
labels:
- widget
- review
- rating
---






| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Yusuf Hendrawan](https://tokopedia.atlassian.net/wiki/people/5df336f3f4ab290ecfc64169?ref=confluence) |
| Product Manager | [Mia Renauly](https://tokopedia.atlassian.net/wiki/people/5dd5ff00a20e0c0e9ef6f8a3?ref=confluence) [I Komang Ananta Aryadinata](https://tokopedia.atlassian.net/wiki/people/6293ecf71a2bdf0070936aeb?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e?ref=directory&src=peopleMenu) |
| Release date | 26 May 2023 / <!--start status:GREY-->MA-3.222<!--end status--> <!--start status:GREY-->SA-2.152<!--end status--> |
| Package Location | `com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating` |
| Widget Class | `WidgetReviewAnimatedRating` |

- [Overview](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2266531458/Review+Star+Rating+Widget#Overview)
- [How To’s](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2266531458/Review+Star+Rating+Widget#How-To%E2%80%99s)
- [Notes](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2266531458/Review+Star+Rating+Widget#Notes)
- [Useful Links](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2266531458/Review+Star+Rating+Widget#Useful-Links)

# Overview

The write review page (single & bulk) has multiple entry points. To unify the entry points to the write review page, a 5-star rating widget has been developed. This widget can be utilized across various entry points, providing a consistent user experience.

![](res/Screen%20Shot%202023-06-06%20at%2014.37.23.png)







[Screen Recording 2023-06-06 at 14.26.22.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.26.22.mov?version=1&modificationDate=1686036409766&cacheVersion=1&api=v2)





[Screen Recording 2023-06-06 at 14.27.31.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.27.31.mov?version=1&modificationDate=1686036475457&cacheVersion=1&api=v2)







[Screen Recording 2023-06-06 at 14.28.47.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.28.47.mov?version=1&modificationDate=1686036580837&cacheVersion=1&api=v2)





[Screen Recording 2023-06-06 at 14.30.00.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.30.00.mov?version=1&modificationDate=1686036642887&cacheVersion=1&api=v2)







[Screen Recording 2023-06-06 at 14.33.10.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.33.10.mov?version=1&modificationDate=1686036815287&cacheVersion=1&api=v2)





[Screen Recording 2023-06-06 at 14.33.48.mov](/wiki/download/attachments/2266531458/Screen%20Recording%202023-06-06%20at%2014.33.48.mov?version=1&modificationDate=1686036863898&cacheVersion=1&api=v2)







# How To’s

#### How To Implement it on an XML-based Page

1. Update your `build.gradle` file to add the `review_common` dependency, then sync the project



```
implementation projectOrAar(rootProject.ext.features.reviewCommon)
```
2. Put the widget config somewhere (ex: ViewModel)



```


private val _widgetConfig = mutableStateOf(createDefaultWidgetConfig())
val widgetConfig: State<WidgetReviewAnimatedRatingConfig>
  get() = _widgetConfig
  
private fun createDefaultWidgetConfig(): WidgetReviewAnimatedRatingConfig {
  return WidgetReviewAnimatedRatingConfig(onStarClicked = ::onStarClicked)
}

private fun onStarClicked(previousRating: Int, currentRating: Int) {
  if (previousRating != currentRating) {
    _widgetConfig.value = _widgetConfig.value.copy(rating = currentRating)
    
    // Some more logic to execute when rating changed
  }
}
```
3. Add `ComposeView` to your view



```
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/review_star_rating"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```
4. Set the `ComposeView` content and pass the widget config



```
private fun setupReviewRatingWidget() {
    binding.reviewStarRating.setContent {
        WidgetReviewAnimatedRating(config = viewModel.widgetConfig.value)
    }
}
```

Please note that the widget is not handling the interaction internally, so you’re responsible for handling the interaction and updating the widget state accordingly. For example, initially, the rating is 0 and then the user clicks on the 3rd star, the `onStarClicked` lambda inside the `widgetConfig` will be invoked and inside that lambda, you need to update the widget state by passing a new `widgetConfig` which has a `rating` value of 3. The widget can be configured by modifying the value of the `widgetConfig`, here's the explanation of each field used to configure the widget:



| **Property** | **Type** | **Description** |
| --- | --- | --- |
| `rating` | `Int` | Represents the number of active stars (the yellow star) on the widget |
| `starSize` | `Dp` | Represents the size of each stars |
| `spaceInBetween` | `Dp` | Represents the gap/space between each star |
| `skipInitialAnimation` | `Boolean` | It's used to toggle on/off the initial animation (the animation from rating 0 to any rating indicated by the `rating` field). You might want to set it to `true` when using this widget inside a `RecyclerView` `ViewHolder` if you don’t want the initial animation always running when the `ViewHolder` is reused by another `RecyclerView` item |
| `onStarClicked` | `(previousRating: Int, currentRating: Int) -> Unit` | It’s a callback that will be called every time the user clicks on any star inside the widget. Here you must handle the widget state update so that the widget reacts accordingly to the user interaction |

Note: You can also check the existing implementation on:

`com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewRating`

# Notes

- The widget is not handling the interaction internally, so you’re responsible for handling the interaction and updating the widget state accordingly. For example, initially, the rating is 0 and then the user clicks on the 3rd star, the `onStarClicked` lambda inside the `widgetConfig` will be invoked and inside that lambda, you need to update the widget state by passing a new `widgetConfig` which has a `rating` value of 3.

# Useful Links



| Figma | <https://www.figma.com/file/XFUosHM26oKy3wuWewRKLH/Star-Rating-on-UOH?type=design&node-id=0-1&mode=design>  |
| --- | --- |




