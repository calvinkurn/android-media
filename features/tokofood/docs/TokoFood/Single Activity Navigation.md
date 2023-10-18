---
title: "Single Activity Navigation"
---


| **Status**      | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                                                                                                                                      |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **PIC**         | [REIVIN OKTAVIANUS](https://tokopedia.atlassian.net/wiki/people/5dae89dab86cd40c2da5ad2f?ref=confluence) [Rizqi Aryansa](https://tokopedia.atlassian.net/wiki/people/5e25ee87006fae0ca232e1ac?ref=confluence) [Yehezkiel .](https://tokopedia.atlassian.net/wiki/people/5c94aa7a7792242c8613ad14?ref=confluence) [Hendry Setiadi](https://tokopedia.atlassian.net/wiki/people/5c94ae68999a3f2d4cae9b85?ref=confluence) |
| Team            | Minion Stuart                                                                                                                                                                                                                                                                                                                                                                                                          |
| Release date    | 30 Jan 2023 / <!--start status:GREY-->MA-3.205<!--end status-->                                                                                                                                                                                                                                                                                                                                                        |
| Module Location | `tkpdabstraction`                                                                                                                                                                                                                                                                                                                                                                                                      |

## Table of Contents

- [Table of Contents](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Table+of+Contents)
- [Overview](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Overview)


	- [Background](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Background)
	- [Project Description](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Project+Description)
- [How-to](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#How-to)


	- [Import](#Import)
	- [Extend Classes](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Extend+Classes)
	- [Provide Launch Modes](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Provide+Launch+Modes)
	- [Navigating](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Navigating)
- [Action Items](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2133920394/Single+Activity+Navigation#Action+Items)

## Overview

### Background

Handling navigation in Android is quite easy. In most cases, we just needed to call `startActivity` for navigating to another page (activity). In Tokopedia project, most of the navigation is being done by using `RouteManager`. There are a lot of logics happening inside the class, but the main outcome is to start the relevant activity from the given applink.  
  
However, there are some cases where navigating using `startActivity` is not enough. In Tokofood project, we found that we need to build a seamless page navigation which shares same view model to handle same data. We would need to implement a way which can handle Tokofood mini cart data while moving around the pages. This can be done by using single activity with multiple fragments.

### Project Description

The outcome of this project is to create a base class (activity and fragment) that provide all the required logics to handle the navigation inside single activity. The classes should be able to:

1. Provide base method to navigate to another single activity fragment.
2. Possess ability to determine whether the destination fragment should be started with single-top or single-task behavior, similar to activity’s launch modes.
3. Handle back stack event automatically in ways that activities do with added animations.

## How-to

### Import

Make sure you pull latest and import `tkpdabstraction` for your object



```
implementation projectOrAar(rootProject.ext.libraries.tkpdabstraction)
```

### Extend Classes

Please extend your activity and fragment classes to `BaseMultiFragActivity` and `BaseMultiFragment`



```
class BaseTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel>
```



```
class MerchantPageFragment : BaseMultiFragment()
```

You can also integrate shared view model to handle shared data across fragments using `HasViewModel` or similar functions.

### Provide Launch Mode

For each fragment that extends `BaseMultiFragment`, you should specify which launch mode that that fragment should possess. Current available modes are:

1. `SINGLE_TASK` → If there is same fragment class in the back stack, the method will pop all the fragments until it reached the fragment index and start a new instance of the fragment.
2. `SINGLE_TOP` → If there is same fragment class in the back stack, the method will just navigate to the same fragment without starting a new instance

In any cases that there is no other fragment with the same class in the back stack, the method will just instantiate a new fragment and add it to the back stack



```
override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
    return BaseMultiFragmentLaunchMode.SINGLE_TASK
}
```

### Navigating

To navigate, call the `navigateToNewFragment` provided from the base class and pass the destination fragment. 



```
navigateToNewFragment(ManageLocationFragment.createInstance())
```



---

## Action Items

- Use `NavController` provided in `Jetpack Navigation`
- Add diagrams in the documentation
