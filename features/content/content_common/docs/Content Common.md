---
title: "Content Common"
labels:
- broadcaster
- ccp
- content
- play
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| **Contributors** | [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence)  |
| Product Manager |  - |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 28 Oct 2022 / <!--start status:GREY-->MA 3.199<!--end status--> <!--start status:GREY-->SA-2.129<!--end status--> |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | - |
| Module Location | `features/content/content_common` |

## Table of Contents

<!--toc-->

## Overview

`content_common` is a module whose purpose is to store reuse-able components that will be used by the modules inside the `content`. In the future, `content_common` is expected to become the only common class in content that will be used to achieve efficiency in using modules. However, this is still under the research.

Currently, in `content_common` we have several widget and components that we can use across the modules inside content. Such as; `onboarding ugc`, `product tag`, `content toolbar` and other that we can explain in here.

## Background

To support the DRY principle, we want to pay more attention to our resources which may still be reused for future implementations. Because it's not only about repetition but resource size will keep increasing if we always create new resources every time we start a new feature. Of course we don't want that to happen.

## Project Description

`content_common` first created was along with Play Broadcaster UGC feature. The idea was raised because we wanted to reuse some of the existing resources without touching any of the existing features. We wanted it to be scalable without breaking anything, so we had to start building a single module that could become a module housing every resource we had. From there, we can also reduce the dependencies between our modules by using only this `content_common`.

There is several feature that fully implemented inside 

`content\_common`.



1. [UGC Onboarding](/wiki/spaces/PA/pages/1992929674)

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20220707-075819.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20220707-080326.png" alt="" />

  
UGC Onboarding is a component where users can do onboarding to complete existing username or accept tnc data. UGC Onboarding was implemented in the form of a bottom sheet so that its use is expected to be reusable for future use. Currently UGC Onboarding is already implemented in User Profile, Play Preparation Live Stream Page & Play Shorts Page.
2. [Product Tag](/wiki/spaces/PA/pages/2054263530)

<img src="https://docs-android.tokopedia.net/images/docs/res/Last%20Purchased%20Section-20220926-062124.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202022-09-26%20at%2012.08.10-20220926-050816.png" alt="" />

  
Product Tag

 is a component that will enable user to select their own product or other product depends on the user role as seller or buyer, and then the product can be attached in their content, whether it is live stream, feed post or shorts video.
3. Content Toolbar

<img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_20230508_162314.png" alt="" />

  
Content Toolbar is a component where user can switch between available account (seller/buyer) using bottom sheet implementation. Currently Content Toolbar is already implemented in:


	1. Play Preparation Live Stream Page
	2. Shorts Video Creation
	3. Create Post for Feed Creation

## Tech Stack

There is several tech stack that we used in this User Profile page. 

- [**Repository**](https://developer.android.com/topic/architecture/data-layer) : a new layer between [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=Cj0KCQjwn9CgBhDjARIsAD15h0Bs5bhR_rURNLecDVevdjE9_NEcDnyW6TAmTy4HgKrYCCTA9YrDvFcaAizrEALw_wcB&gclsrc=aw.ds) & [UseCase](https://developer.android.com/reference/androidx/camera/core/UseCase). The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- [**MVI**](https://medium.com/swlh/mvi-architecture-with-android-fcde123e3c4a) : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- [**Flow**](https://developer.android.com/kotlin/flow): previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only. [Click here to see the official documentation.](https://developer.android.com/kotlin/flow)
- [**Fragment Factory**](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory): Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor. [Click here to see the official documentation.](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory)

## Flow Diagram

1. UGC Onboarding

  


<img src="https://docs-android.tokopedia.net/images/docs/res/Untitled%20Diagram%20%285%29.jpg" alt="" />
2. Product Tag

<img src="https://docs-android.tokopedia.net/images/docs/res/Untitled%20Diagram%20%283%29.jpg" alt="" />

## How-to

There is actually some resource that fully implemented inside `content_common` so we need to know how to for that thing.

1. UGC Onboarding

  
First thing first, we need to add the dependency to `content_common` module inside `build.gradle your module`.



```
  implementation project(rootProject.ext.features.contentCommon)
```

  
Then all you need todo is just open the bottom sheet like this implementation inside 

`UserProfileFragment.kt`



```
private fun openUGCOnboardingBottomSheet() {
    childFragmentManager.executePendingTransactions()
    val existingFragment = childFragmentManager.findFragmentByTag(UGCOnboardingParentFragment.TAG)
    if (existingFragment is UGCOnboardingParentFragment && existingFragment.isVisible) return

    val bundle = UGCOnboardingParentFragment.createBundle(
        if (viewModel.profileUsername.isEmpty()) UGCOnboardingParentFragment.OnboardingType.Complete
        else UGCOnboardingParentFragment.OnboardingType.Tnc
    )
    childFragmentManager.beginTransaction()
        .add(UGCOnboardingParentFragment::class.java, bundle, UGCOnboardingParentFragment.TAG)
        .commit()
}
```

  
Notice that there is `bundle` that we need to sent. This is for determining which onboarding bottom sheet that will appear, whether the TNC or the Username.



```
val bundle = UGCOnboardingParentFragment.createBundle(
    if (viewModel.profileUsername.isEmpty()) UGCOnboardingParentFragment.OnboardingType.Complete
    else UGCOnboardingParentFragment.OnboardingType.Tnc
)
```
2. Product Tag


  
How to integrate[How to Reuse Content Product Picker UGC](/wiki/spaces/PA/pages/2073495583/How+to+Reuse+Content+Product+Picker+UGC)   
How to use the sample bottom[How to Use Content Product Picker UGC Sample in TestApp](/wiki/spaces/PA/pages/2073496162/How+to+Use+Content+Product+Picker+UGC+Sample+in+TestApp)
3. Content Toolbar  
First thing first, we need to add the dependency to `content_common` module inside `build.gradle your module`.



```
  implementation project(rootProject.ext.features.contentCommon)
```

  
Then you need to add this ui element in your `.xml` file.



```
<com.tokopedia.content.common.ui.toolbar.ContentAccountToolbar
        android:id="@+id/toolbar_content_common"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
```

  
Afther that all you need to do is just call and init the data using this kind of implementation like `PlayBroadcastPreparationFragment.kt` do.



```
binding.toolbarContentCommon.apply {
  navIcon = CLOSE
  setCustomizeContentColor(ContentColor.TRANSPARENT, false)

  setOnBackClickListener {
    analytic.clickCloseOnPreparation()
    activity?.onBackPressed()
  }
}

(...)

with(binding.toolbarContentCommon) {
  title = getString(contentCommonR.string.feed_content_live_sebagai)
  subtitle = state.name
  icon = state.iconUrl
}
```

  
To manage the coachmark, you can do something like this



```
with(binding.toolbarContentCommon) {
    if (parentViewModel.isAllowChangeAccount) {
    setOnAccountClickListener {
    analytic.onClickAccountDropdown()
    hideCoachMarkSwitchAccount()
    openAccountBottomSheet()
  }
} else {
  setOnAccountClickListener(null)
  }
}

(...)

if(parentViewModel.isAllowChangeAccount && viewModel.isFirstSwitchAccount) {
 add(
    CoachMark2Item(
      anchorView = binding.toolbarContentCommon,
      title = getString(contentCommonR.string.sa_coach_mark_title),
      description = getString(contentCommonR.string.sa_livestream_coach_mark_subtitle),
      position = CoachMark2.POSITION_BOTTOM,
    )
  )
  viewModel.setNotFirstSwitchAccount()
}
```

## Action Items

- utilize which class/files/resources that need to be put inside `content_common` which not
- try to clean up the package files

## Useful Links

- Product Picker UGC documentation[[Android] Content Product Picker UGC](/wiki/spaces/PA/pages/2054263530) , [How to Use Content Product Picker UGC Sample in TestApp](/wiki/spaces/PA/pages/2073496162/How+to+Use+Content+Product+Picker+UGC+Sample+in+TestApp) & [How to Reuse Content Product Picker UGC](/wiki/spaces/PA/pages/2073495583/How+to+Reuse+Content+Product+Picker+UGC)
- User Profile Onboarding documentation[[Android] User Profile Onboarding](/wiki/spaces/PA/pages/1992929674) & [How to Reuse Feed UGC Onboarding Bottom Sheet](/wiki/spaces/PA/pages/1998717550/How+to+Reuse+Feed+UGC+Onboarding+Bottom+Sheet)

## FAQ

<!--start expand:What is content_common?-->
`content_common` is a module whose purpose is to store reuse-able components that will be used by the modules inside the `content`. In the future, `content_common` is expected to become the only common class in content that will be used to achieve efficiency in using modules.
<!--end expand-->

<!--start expand:What inside content_common?-->
There is several feature that fully implemented inside `content_common`; Play Broadcaster UGC Onboarding, Play Broadcaster Product Tag & Content Toolbar.
<!--end expand-->

<!--start expand:How to use content_common?-->
We only need to implement this line inside `build.gradle` your module.



```
  implementation project(rootProject.ext.features.contentCommon)
```
<!--end expand-->

