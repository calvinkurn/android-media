---
title: "Stories Widget"
labels:
- stories
- content
- entry-point
- widget
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence)  |
| Product Manager | [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](/wiki/spaces/PA/pages/1939344716/Android+Minion+Lance) |
| Release date | 27 Oct 2023 / <!--start status:BLUE-->MA-3.240<!--end status--> <!--start status:GREEN-->SA-2.170<!--end status--> |
| Module type | <!--start status:PURPLE-->SUB-FEATURE<!--end status--> |
| Module Location | `features.storiesWidget` | `features/content/stories_widget` |
| PRD | [PRD - Automated Stories (Shop Updates)](/wiki/spaces/CN/pages/2260959233) |

## Release Notes

<!--start expand:27 Oct 2023 (MA-3.240/SA-2.170)-->
First Release

<https://github.com/tokopedia/android-tokopedia-core/pull/35540>
<!--end expand-->

## Overview

**Stories Widget** is one of the content widget that displays a circle outside shop / user profile picture if they have uploaded stories content. This widget is attached to profile picture across Tokopedia pages as entry point to stories viewing room.

### Background

Stories Widget acts as a entry point to stories viewing room. Mainly, it’s attached to profile picture in any pages (e.g. Shop Page, Top Chat, PDP, etc). Since it should be used within other modules, we make a dedicated module that contains stories widget components & make it as easiest as possible to implement.

### Project Description

Stories widget has 3 main states: **No Stories**, **Has Unseen Stories** & **All Stories Seen**, see the images below:

image-20240122-085236.png

image-20240122-084258.png

image-20240122-084318.png

No Stories state will show no border, and the rest will show a border with different color, depends on the state. Only **has unseen stories** & **all stories seen** widget are clickable, and if user clicks the profile picture, it will redirect user to stories viewing room and display the contents from that particular user.

Stories widget can be attached to view-based (XML) only. If you want to implement it in Jetpack Compose component, please consult with `@android-minionlance-dev` in Slack.

## Tech Stack

- [**ViewModel**](https://developer.android.com/topic/libraries/architecture/viewmodel) : Used to store stories widget view logic.
- [**Flow**](https://developer.android.com/kotlin/flow) : Used to make stories widget data observable.
- [**Animation**](https://developer.android.com/develop/ui/views/animations/overview) : Used to animate the widget border when user firstly sees the widget.
- [**View Binding**](https://developer.android.com/topic/libraries/view-binding) : Make accessing the view easier.
- [**Remote Config**](https://firebase.google.com/docs/remote-config) : To dynamically change stories widget behavior without app release, mainly used to enable / disable stories widget.

## Navigation

Please refer to this [**doc**](/wiki/spaces/PA/pages/2330069074/Entry+Points) to see all the existing pages / modules that have implemented Stories Widget.

## How-to

1. Add module dependency



```
dependencies {
  ...
  implementation projectOrAar(rootProject.ext.features.storiesWidget)
}
```
2. Initialize StoriesWidgetManager by using `activityStoriesManager` or `storiesManager` delegate.



```
class YourFragment : Fragment() {

    private val storiesManager by storiesManager(StoriesEntryPoint.YourPage) {
        /** All the configurations below are optional */
        setAnimationStrategy(OneTimeAnimationStrategy())
        setCoachMarkStrategy(NoCoachMarkStrategy())
        setScrollingParent(...)
        setTrackerBuilder(...)
        setTrackerSender(...)
    }
}
```
3. Wrap your profile picture view with `StoriesWidgetLayout`



```
<com.tokopedia.stories.widget.StoriesWidgetLayout
      android:id="@+id/image_shop_container"
      android:layout_width="68dp"
      android:layout_height="68dp"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      >
  
      <androidx.appcompat.widget.AppCompatImageView
          android:id="@+id/image_shop_logo"
          android:layout_width="64dp"
          android:layout_height="64dp"
          android:layout_gravity="center"
          android:layout_margin="4dp"
          android:adjustViewBounds="true"
          android:background="@drawable/bg_shop_page_profile"
          android:contentDescription="@string/content_desc_shop_page_main_profile_image"
          android:scaleType="centerCrop"
          app:unify_image_type="circle"
          />

</com.tokopedia.stories.widget.StoriesWidgetLayout>
```
4. Call `manage()` function to setup widget initial behavior.



```
fun setup(shopId: String) {
    storiesManager.setup(binding.imageShopContainer, shopId)
}
```
5. To update stories state, you need to call `updateStories` and pass the Author ID (shop ID / user ID).



```
fun updateStoriesWidget(shopId: String) {
    storiesManager.updateStories(listOf(shopId))
}
```



---

## Action Items

*No action items yet.*

## Useful Links

- [Stories - PRD](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2260959233/PRD+-+Automated+Stories+Shop+Updates)
- [Stories - Figma](https://www.figma.com/file/sYnxbgXNJ2XepdKuCyStMj/%5BPlay%5D-Story---Mobile?type=design&mode=design&t=4hN9TbJELZw9wRt9-0)
- [Stories - Entry Point](/wiki/spaces/PA/pages/2330069074/Entry+Points)

## FAQ

<!--start expand:Can I implement this widget in Jetpack Compose component?-->
We haven’t developed the Compose version yet, but it’s possible even though we need to do work around. Please consult with `@android-minionlance-dev` through Slack.
<!--end expand-->

