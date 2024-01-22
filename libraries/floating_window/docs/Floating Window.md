---
title: "Floating Window"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| **Contributors** | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Hendry Setiadi](https://tokopedia.atlassian.net/wiki/people/5c94ae68999a3f2d4cae9b85?ref=confluence)  |
| Product Manager | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 17 Jan 2021 / <!--start status:BLUE-->MA-3.109<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Product PRD | [Release Docs](/wiki/spaces/PA/blog/2021/02/11/1237583958/PiP+-+behind+the+scenes) | [Background](/wiki/spaces/CN/pages/907609510/Play+-+Picture+in+Picture+Analysis) |
| Module Location | `libraries.floatingWindow` | `libraries/floating_window` |

<!--toc-->

## Release Notes

<!--start expand:17 Jan 2021 (MA-3.109)-->
Released



PR : <https://github.com/tokopedia/android-tokopedia-core/pull/16884>
<!--end expand-->

## Overview

**Floating Window** is an internal library that allows developers to implement a view that can draw over other apps in a simple way. Floating Window was first created for the purpose of enabling Picture-in-Picture video of Tokopedia Play. Please be mindful when you are using this as this is not something that is recommended to be used in a normal situation.

### Background

Sometimes, we need to be able to show something in Tokopedia App that will stay on the screen regardless of the current Activity shown. There are actually two ways to do that in Android:

1. [Official Android Picture-in-Picture](https://developer.android.com/develop/ui/views/picture-in-picture)
2. [Floating window](https://www.geeksforgeeks.org/how-to-make-a-floating-window-application-in-android/)

The problem with the first option is that there are some constraints that we need to abide, e.g. minimum OS supported is Android Oreo, while the second option does not have too many constraints. Since Tokopedia App is used by many people with vast different devices (from low end to high end), we need to make sure that all people (or at least most people) can have the same experience regardless of their device, which is why it is important that we need to use the library that has the most minimum constraints, (which is the second option).

The problem with the second option is that it allows views to draw over other apps (not just Tokopedia Apps) and it also requires dangerous permission to notify the user that it might breach their privacy. Since it is not recommended, we need to limit the possibility of developers breaching the users' privacy when they need to use floating window in their features.

### Project Description

Floating window is a library that allows developers to implement floating window in a more simple way and it has also prevent the views drawn to be removed when the Tokopedia App is not on foreground.

This library has also provided some helpers so that the developer can easily request the `Draw over other Apps` permission to the user and the developer can also easily move the views just by implementing one function.

<img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_1675236116.png" alt="" />

## Tech Stack

- [**ProcessLifecycleOwner**](https://developer.android.com/reference/androidx/lifecycle/ProcessLifecycleOwner) : a lifecycle owner for the application/process, so that we can easily track whether the application is currently active on the foreground or not.

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Floating%20Window%20Flow.drawio%20%281%29.png" alt="" />

## How-to

1. Create a custom View to be shown





```
class MyCustomView : View(context: Context) {
  //custom your view however you want
}
```

2. Create a `FloatingWindowView` instance



```
private val myView = MyCustomView(context)

private val floatingWindowView: FloatingWindowView = FloatingWindowView.Builder(
    key = MY_CUSTOM_UNIQUE_KEY, //your unique key, can be used to remove the floating window later
    view = myView, //your custom view
    width = (0.6f * getScreenWidth()).toInt(), //the width of the floating window
    height = (0.6f * getScreenHeight()).toInt(), //the height of the floating window
).setX(getScreenWidth() - 50) // the X coordinate
.setY(getScreenHeight() - 50) // the Y coordinate
.build()
```

3. (Optional) Implement `FloatingWindowView#doOnDragged` if you want it to be draggable



```
floatingView.doOnDragged { layouter, requestedCoordinate ->
    val newCoordinate = Point()
    val startLimit = 16
    val endLimit = layouter.screenWidth - layouter.viewWidth - 16
    val topLimit = 16
    val bottomLimit = layouter.screenHeight - layouter.viewHeight - 16

    newCoordinate.x = when {
        requestedCoordinate.x > endLimit -> endLimit
        requestedCoordinate.x < startLimit -> startLimit
        else -> requestedCoordinate.x
    }

    newCoordinate.y = when {
        requestedCoordinate.y > bottomLimit -> bottomLimit
        requestedCoordinate.y < topLimit -> topLimit
        else -> requestedCoordinate.y
    }

    layouter.updatePosition(newCoordinate.x, newCoordinate.y)
}
```

4. Create a `FloatingWindowAdapter` instance inside Activity/Fragment



```
//For Fragment
class MyFragment : Fragment() {
    private val adapter: FloatingWindowAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FloatingWindowAdapter(this@MyFragment)
    }
}

//or for Activity
class MyActivity : AppCompatActivity() {
    private val adapter: FloatingWindowAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FloatingWindowAdapter(this@MyActivity)
    }
}
```

5. Call `FloatingWindowAdapter#onActivityResult` inside Activity’s/Fragment’s onActivityResult



```
//Call this inside the Activity/Fragment that you passed on the FloatingWindowAdapter constructor
//when you created the adapter instance
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    adapter.onActivityResult(requestCode, resultCode, data)
    super.onActivityResult(requestCode, resultCode, data)
}
```

6. Call `FloatingWindowAdapter#addView` to show FloatingWindow



```
adapter.addView(
        floatingView = floatingView,
        //if you passed this as true, it means that you want to overwrite the existing floating window with another floating window
        //if the key matches (e.g. MY_CUSTOM_UNIQUE_KEY)
        overwrite = true,
        onSuccess = {
            //this will be called when all the required permissions are allowed
            //and FloatingWindow has successfully shown the view
        },
        onFailure = { exception: FloatingWindowException ->
            //this will be called when any of the required permissions are rejected
        },
        onShouldRequestPermission = { permissionFlow: FloatingWindowPermissionManager.RequestPermissionFlow ->
            //this will be called when the permission has not been allowed
            //and you wanted to guide the user to request the permission
            
            //Call FloatingWindowPermissionManager.RequestPermissionFlow#requestPermission to guide user to the permission page 
            permissionFlow.requestPermission()
            
            //or call FloatingWindowPermissionManager.RequestPermissionFlow#cancel to cancel the permission flow
            //permissionFlow.cancel()
        }
)
```

7. If all is good and permission(s) are allowed, the view will automatically be shown. But if the permission(s) are not allowed, then it would be best to give feedback to the user as to why the permission is required for this case.

8. (Extra) If you want to remove existing Floating Window view, you can remove that by calling `FloatingWindowAdapter#removeByKey(key)` where key is the key that you provided into `FloatingWindowView.Builder` (step 2).



```
adapter.removeByKey(MY_CUSTOM_UNIQUE_KEY)
```

## FAQ

<!--start expand:Can I use Floating Window library to show views outside of Tokopedia App?-->
Currently, it is not supported as it can be considered as a security risk to our users
<!--end expand-->

