---
title: "Play Common: Common module for most things related to Play"
labels:
- content
- play
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Contributors** | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Product Manager | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 10 Feb 2020 / <!--start status:GREY-->MA-3.61<!--end status--> <!--start status:GREY-->SA-1.62<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Module Location | `features.play_common` | `features/content/play_common` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:12 Oct (MA-1.391/SA-2.91)-->
###### *You can describe the release notes according to the app version that shipped your updates. Let’s make it descending based on the date and please define the app version for both MA and SA.*
<!--end expand-->

## Overview

Play Common is a module that contains common codes that are used in other Play features module such as Play Viewer (`features/content/play`), Play Broadcaster (`features/content/play_broadcaster`), and Play Widget (`features/content/play_widget`).

This module contains several GQL use cases, common views, and some important util classes that are all used across Play feature modules.

This module can also be used by other feature modules outside of Play if those modules need to use some use cases from Play.

### Background

Play common exists because in Tokopedia, there are some common understandings that:

- a feature cannot depends on another feature,
- do not create GQL use case that has the same query if it has already existed,
- reduce duplicate code as much as possible (to reduce app size as much as possible).

Knowing those points and the nature of Play modules where there are so many things similar between Play Viewer and Play Broadcaster (UI and util classes), it was decided that a common module was needed. Thus, this module is created to satisfy all the requirements above.

### Project Description

Play common module contains:



- Common views:


	- Interactive/game views (Giveaway and Quiz, Leaderboard)
	- Rounded ViewGroups (`RoundedConstraintLayout`, `RoundedFrameLayout`, etc)
- Kotlin extensions:


	- `InsetsExt.kt` (Related to handling Insets)
	- `Combine.kt` (Defines neat function for combining more than 5 Flows)
	- `LifecycleExt.kt` (Provides handy functions related to Lifecycle, such as suspending function to wait for certain lifecycle)
	- `PlayCommonExt.kt` (Contains most of the handy extensions for keyboard, view, fragment, etc)
- GQL:


	- Interactive use case (Get current interactive, get leaderboard)
	- Channel use case (Update channel status)
- Util:


	- `PlayWebSocket` (Websocket for Play Viewer and Play Broadcaster)
	- `PlayChannelSSE` (SSE for Play Viewer upcoming channel)
	- `ImageBlurUtil` (Helper class to blur image)
	- `PlayVideoWrapper` (Wrapper class for ExoPlayer)
	- `PlayToaster` (Wrapper class for Toaster)
	- `ViewComponent` (A class to help separation of view’s logic code from Activity or Fragment)
	- `EventBus` (A regular event bus that utilizes `StateFlow`)
	- `PlayShortsUploader` (A helper class for uploading shorts video & upload notification)

## Tech Stack

- [ExoPlayer](https://exoplayer.dev/): this feature uses ExoPlayer because video is the core unit in Play features. ExoPlayer is created by Google and works really well in Android, especially for playing HLS media file.
- [Lottie](https://github.com/airbnb/lottie-android): this feature uses Lottie to play some animations that are needed in Interactive views. Lottie is created by AirBnb and is really easy to use and integrate to Play feature.
- [Coroutines](https://developer.android.com/kotlin/coroutines): this feature uses Coroutine (suspending function and Flow) in most of the util classes as all Play features are fully using Coroutine.

## How-to

### Extensions and Util

###### 1. [Insets] Apply insets listener to a View



```
myView.doOnApplyWindowInsets { view, insets, padding, margin ->
    //use insets parameter to get current insets
    //use padding to get the initial padding of this view (not the updated padding)
    //use margin to get the initial margin of this view (not the updated margin)
    
    //e.g. update top padding
    view.updatePadding(top = insets.top + padding.top) 
}
```

###### 2. [Insets] Request update insets



```
myView.requestApplyInsetsWhenAttached()
```

###### 3. [Coroutine, Flow] Combine more than 5 flows



```
combine(flow1, flow2, flow3, flow4, flow5, flow6) {
  value1, value2, value3, value4, value5, value6 ->
    MyState(
      value1, value2, ...
    )
}
```

###### 4. [Lifecycle] Waiting for lifecycle to resume



```
lifecycleOwner.lifecycleScope.launch {
  lifecycleOwner.awaitResume() //suspending function
}
```

###### 5. [View, Layout, ConstraintLayout] Change Constraint of ConstraintLayout easier



```
myConstraintLayout.changeConstraint {
  //the `this` inside this block refers to ConstraintSet
  //so you can directly call ConstraintSet functions
  
  connect(ConstraintSet.TOP, firstViewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID)
}
```

###### 6. [View] Waiting for View to have height > 0 and width > 0



```
scope.launch {
  view.awaitMeasured()
}
```

###### 7. [View] Waiting for ViewTreeObserver of this View to call the next onGlobalLayout



```
scope.launch {
  view.awaitNextGlobalLayout()
}
```

###### 8. [View] Handy function to do something on the next call on View’s pre-draw



```
view.doOnPreDraw {
  //do something pre draw
}
```

###### 9. [View] Get View’s global visible Rect



```
view.globalVisibleRect //will get visible bounds of the view as Rect object
```

###### 10. [Fragment] Redraw/Recreate whole Fragment’s View



```
myFragment.recreateView()
```

###### 11. [Flow] Collect Flow’s values as a Pair of previous value and current value



```
val myFlow = MutableStateFlow<String>("hello world")

myFlow.withCache().collectLatest { (prevString, currString) ->
    //you can get the previous value (prevString) and current value (currString)
}
```

###### 12. [Performance] Get performance class config of user’s device



```
val performanceClassConfig = PerformanceClassConfig(context)

val class: Int = performanceClassConfig.getDevicePerformanceClass()
//class 0 -> Low performance class
//class 1 -> Average performance class
//class 2 -> High performance class
```

###### 13. [View, RecyclerView] Immediately stop the excess fling of RecyclerView after fling



```
recyclerView.addOnScrollListener(StopFlingScrollListener())
```

###### 14. [Date] Format date that is commonly used in Play



```
val outputDateString = PlayDateTimeFormatter.format(
  inputDateString,
  PlayDateTimeFormatter.dMMMMyyyy, //date format of the input
  PlayDateTimeFormatter.yyyyMMddTHHmmss, //date format of the output
)
```

### Kotlin Delegate

###### 1. [Job, Coroutine] Reuse job easily



```
val job by reusableJob()

job = scope.launch {
  //do x
}

job = scope.launch {
  //this automatically cancel the current active job assigned to the variable
  //do y
}
```

### Play Toaster

###### 1. Create PlayToaster instance



```
val toaster = PlayToaster(view, lifecycleOwner)
```

###### 2. Show error toaster easily



```
toaster.showError(
  theThrowable,
  customErrorMessage,
  duration,
  actionLabel,
  actionListener,
  bottomMargin,
)
```

###### 2. Show regular/any type of toaster easily



```
toaster.showToaster(
  message,
  toasterType,
  duration,
  actionLabel,
  actionListener,
  bottomMargin,
)
```

### WebSocket

###### 1. Create socket instance



```
val socket = PlayWebSocketImpl(
  OkHttpClient.Builder(),
  userSession,
  dispatchers,
  context,
  LocalCacheHandler(),
)
```

###### 2. Get Token from gql `playGetSocketCredential`



```
//inside CoroutineScope or a suspending function

val useCase = GetSocketCredentialUseCase(graphqlRepository)
val token = useCase.executeOnBackground()
```

###### 3. Connect to a channel Id



```
socket.connect(
  channelId,
  warehouseId, //optional, used for tokonow channel
  token, //for non login user, use empty string,
  source,
)
```

###### 4. Call `listenAsFlow()` to listen to all incoming messages from the socket



```
socket.listenAsFlow().collect {
  //Collect incoming message
}
```

###### 5. Send a message to the existing websocket connection



```
socket.send(messageInStringFormat)
```

###### 6. Call `close()` to close the WebSocket connection



```
socket.close()
```

### SSE (Server Sent Event)

###### 1. Create SSE instance



```
val sse = PlayChannelSSEImpl(
  userSession,
  dispatchers,
  context,
)
```

###### 2. Connect to a channel Id



```
sse.connect(
  channelId,
  source,
  token, //for non login user, use empty string,
)
```

###### 3. Call `listen()` to listen to all incoming messages from the SSE



```
sse.listen().collect {
  //Collect incoming message
}
```

###### 4. Call `close()` to close the SSE connection



```
sse.close()
```

### Event Bus

###### 1. Create EventBus Instance with specified type



```
val bus = EventBus<Any>()
```

###### 2. Broadcast something to event bus



```
bus.emit(broadcastedData)
```

###### 3. Subscribe to event bus



```
bus.subscribe().collect {
  //collect broadcasted data here
}
```

### Image Blur

###### 1. Create ImageBlurUtil instance



```
val blurUtil = ImageBlurUtil(context)
```

###### 2. Create ImageBlurUtil instance



```
scope.launch {
  blurUtil.blurImage(bitmap, blurRadius)
}
```

### ViewComponent

###### 1. Create a class that extends ViewComponent



```
class MyViewComponent(container: ViewGroup) : ViewComponent(container, R.id.my_view) {
  //view logic
}
```

###### 2. Initialize the class by calling `by viewComponent` inside Fragment or Activity



```
private val myViewComponent by viewComponent { MyViewComponent(it) }
```

###### 3. Now you have access to handy function in the ViewComponent



```
class MyViewComponent(container: ViewGroup) : ViewComponent(container, R.id.my_view) {
  //view logic
  
  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
      doSomethingOnResume()
  }
  
  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
      doSomethingOnDestroy()
  }
}
```

### PlayVideoWrapper

###### 1. Create a PlayVideoWrapper instance by using the builder



```
val videoWrapper = PlayVideoWrapper.Builder(context).build()
```

###### 2. Play video from Uri



```
videoWrapper.playUri(
  uri,
  isAutoPlayAfterLoadFinished,
  bufferControl,
  vodStartPosition,
)
```

###### 3. Get the ExoPlayer instance



```
val exoPlayer = videoWrapper.videoPlayer
```

###### 4. Pause the video player



```
videoWrapper.pause()
```

###### 5. Resume the video player



```
videoWrapper.resume()
```

###### 6. Stop the video player



```
videoWrapper.stop()
```

###### 7. Release the video player



```
videoWrapper.release()
```

###### 8. Mute the video player



```
videoWrapper.mute(shouldMute = true)
```

###### 9. Add listener



```
videoWrapper.addListener(object : PlayVideoWrapper.Listener {

  override fun onPlayerStateChanged(videoWrapper: PlayVideoWrapper, state: PlayVideoState) {
    //called every video state changed (e.g. paused, ended, idle)
  }
  
  override fun onVideoPlayerChanged(player: ExoPlayer) {
    //called every time a new exoplayer instance is spawned and used
  }
  
})
```

### PlayShortsUploader

###### 1. Include `PlayShortsUploaderModule.kt` in your Dagger Component.



```
@Component(
    modules = [
        ...
        PlayShortsUploaderModule::class, // add this line
        ...
    ], 
    dependencies = [BaseAppComponent::class]
)
interface YourComponent{
    ...
}

```

###### 2. Start uploading your play shorts using PlayShortsUploader



```
@Inject
lateinit var playShortsUploader: PlayShortsUploader

...

fun uploadPlayShorts() {
  val uploadModel = PlayShortsUploadModel(...)
  val playShortsUploader.upload(uploadModel)
}
```

###### 3. Observe the Play Shorts Uploader status in your view.



```
@Inject
lateinit var playShortsUploader: PlayShortsUploader

...

fun observePlayShortsUploader() {
  playShortsUploader.observe(viewLifecycleOwner) { progress, uploadData ->
    when (progress) {
      PlayShortsUploadConst.PROGRESS_COMPLETED -> {
        // success
      }
      PlayShortsUploadConst.PROGRESS_FAILED -> {
        // error
      }
      else -> {
        // in progress
      }
    }
  }
}
```



---

