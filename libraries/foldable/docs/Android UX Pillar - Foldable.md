---
title: "Android UX Pillar - Foldable"
labels:
- foldable-devices
- android-foldable
- foldablesupportmanager
- foldable
---


| **Status** | <!--start status:PURPLE-->MERGED<!--end status--> |
| --- | --- |
| **Project Lead** | [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence)  |
| Product Manager | Android Internal Team |
| Team | [Darth](https://tokopedia.atlassian.net/people/team/8c90de56-d4f1-45a7-9021-bd87c4ea9ce2) ([Mayank Gera](https://tokopedia.atlassian.net/wiki/people/5f9281a8f162650070dacffd?ref=confluence) ) |
| Release date<br/> | 19 May 2023 / <!--start status:GREY-->MA-3.219<!--end status-->  |
| Module type |  <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status--> |
| Product PPT | <https://docs.google.com/presentation/d/1YhSDnG8GgzCa9b7O9U93MTgJk1yk55ZArtBpwTlhmfE/edit?usp=sharing>  |
| Module Location |  `libraries/foldable` |

## Table of Contents

<!--toc-->

## Overview

In the world of Android, we have a very known problem. The problem of different screen sizes and densities. We overcame those problems by making our layouts more responsive toward these parameters. Be it using density dependent sizes, using constraint layout for maintain ratios with respect to screen size rather than hardcoding these dimensions. But now with advancement of tech we have a new screen type to handle. The foldable.   
Foldable devices provide an opportunity for innovative app development. Large and small screens on the same device offer complementary but distinct interactive experiences.

### Project Description

We have created a library here for our Nakama's to help support Foldable devices in their modules. Please view the [Presentation](https://docs.google.com/presentation/d/1YhSDnG8GgzCa9b7O9U93MTgJk1yk55ZArtBpwTlhmfE/edit?usp=sharing) if you are new to Foldables to get basic idea of foldable handling provided by android and our Library.

## How-to

Add the dependency for adding Foldable Support in your module



```
implementation project(rootProject.ext.libraries.foldable)
```

To get foldable callbacks and support, simply initialise the SupportManager in onCreate of your activity.



```
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.foldable_activity_layout)
  constraintLayout = findViewById(R.id.parent_container)
  FoldableSupportManager(foldableInfoCallback = this, activity = this)
}
```


Listeners to be passed and used :



```
interface FoldableInfoCallback {
  fun onChangeLayout(foldableInfo: FoldableInfo)
}
```


The FoldableInfo class provides you with information for callbacks received in windowLayoutInfo, the FoldingFeature if present inside windowLayoutInfo and many methods built on top of the folding feature to help handle states of foldable device.

FoldableInfo provides access to important information related to the device over FoldingFeature:

- `isFoldableDevice()`: Returns true if current device is foldable otherwise false
- `isTableTopMode()`: Returns true if device is currently in TableTop orientation.
- `isBookMode()`: Returns true if device is currently in orientation.
- `isHalfOpen()`: Returns true if device is in half opened state
- `isFlat()`: Returns true if device is in flat state
- `getDisplayFeatureBounds(rootView: View,includePadding: Boolean = true)`:  Get the bounds of the display feature translated to the View's coordinate space and current position in the window as Rect
- `alignSeparatorViewToFoldingFeatureBounds( constraintSet: ConstraintSet,        rootView: View, separatingViewID: Int)`: Returns ConstraintSet with the seperatingView aligned around the foldable feature.



---

![](res/_IiKPq5UytGBfQaoViqY-NgdVmPuePQqbzGKT6ECFlZhsMtMpZ3E6A3-nxc4ZNoEIt_p9gILTyKDqrlPVeQm7OeQpvdotJgjaHABuOeahL2Xj3BTD7pRFymobJ-lSif28yIsUGt0dCONpr4KZzjzuI8Xqe6GKlMhFQH0tE5vBYyCnrffiDMOngFIl_c)

