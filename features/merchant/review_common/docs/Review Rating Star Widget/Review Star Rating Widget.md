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







[Screen Recording 2023-06-06 at 14.26.22.mov](https://api.media.atlassian.com/file/7fe57a59-74c6-4b22-b4f6-ee513206c030/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)





[Screen Recording 2023-06-06 at 14.27.31.mov](https://api.media.atlassian.com/file/964f3267-e843-444c-b1dd-56f2a1826790/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)







[Screen Recording 2023-06-06 at 14.28.47.mov](https://api.media.atlassian.com/file/20ddbd81-ae04-4abd-900e-7114e53e6060/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)





[Screen Recording 2023-06-06 at 14.30.00.mov](https://api.media.atlassian.com/file/4d8da337-3ab2-4529-b03c-52fe04060204/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)







[Screen Recording 2023-06-06 at 14.33.10.mov](https://api.media.atlassian.com/file/a5172581-fbde-4bd1-b326-ac63e30d4b80/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)





[Screen Recording 2023-06-06 at 14.33.48.mov](https://api.media.atlassian.com/file/90c76d9e-7974-4d07-af2c-bfc23692041a/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2266531458&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMjY2NTMxNDU4IjpbInJlYWQiXX0sImV4cCI6MTY5NTYyNDcwMCwibmJmIjoxNjk1NjIxODIwfQ.WDlH4nf7YcmKEtN9Z5hBnytyMOjXbJHc3BTybqw7PVA)







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




