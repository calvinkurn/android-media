---
title: "Play Widget"
---






| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence)  |
| Product Manager | [Calvin Alfares Lamda Saputra](https://tokopedia.atlassian.net/wiki/people/60bdaf042bd21400692423d7?ref=confluence) [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | <!--start status:GREEN-->MA-3.99<!--end status--> <!--start status:GREEN-->SA-2.29<!--end status--> Jumbo & Large widget since <!--start status:GREEN-->MA-3.167<!--end status--> |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status--> |
| Module Location | `features.play_widget` | `features/content/play_widget` |

## Table of Contents

<!--toc-->

## Release Notes (
max 5 latest release notes)

<!--start expand:10 Feb (MA-3.209/SA-2.139)-->
<!--start status:GREEN-->NEW<!--end status--> Play Grid Type.  
Jira: 

 

 




 
 [CNGC-4184](https://tokopedia.atlassian.net/browse/CNGC-4184)
 -
 Getting issue details...

STATUS




  
PR: <https://github.com/tokopedia/android-tokopedia-core/pull/31460>
<!--end expand-->

## Overview

Play Widget is mostly everywhere in Tokopedia apps. When you open Tokopedia, you might see Play Widget in Home, When you’re browsing in Feed, you see it everywhere, on top of it, in video tab as well. When you open product detail page, you can see it as well. If the seller has VOD content or currently do live-streaming or scheduled / upcoming video, you can see it all on their shop page. Play Widget basically to make users more aware about our Play contents.

### Background

In order to easily distribute Play's contents, a standardized widget is necessary to both attract users to view the contents & to brand the availability of PLAY

 (live streaming contents - from [Play Broadcaster](/wiki/pages/createpage.action?spaceKey=PA&title=%5Barchived%5D%20Play%20Broadcaster%20%28Hydra%29&linkCreation=true&fromPageId=989043703) feature & short video on demand - from [Play Shorts](/wiki/spaces/PA/pages/2156695768/Play+Shorts) ) inside Tokopedia.

### Project Description

Play Widget is a plug-and-play library. Other devs from another module could easily put Play Widget in their page with this module. No need to worry, we handle it all.

1. Auto-refresh configurable from FE.
2. Toggle for on / off autoplay for video, docs: [Enable / Disable Auto-Play on Play Widget](/wiki/spaces/PA/pages/1992460501)
3. Optimize video resolution for video, docs: [Lower Video Resolution for Play Widget](/wiki/spaces/PA/pages/2082841996/Lower+Video+Resolution+for+Play+Widget)

Play Widget, can be seen at:

- Home
- PDP (product detail page)
- Shop Page
- Discovery Page
- Feed (Explore tab and Video tab)
- User Profile
- Play Room (Explore Widget)

Several types of widget

:

**Type of widget [each card]**



| **Video Card**<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-07-03%20at%2015.44.45.png" alt="" /><br/> | **Banner**<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-07-03%20at%2016.50.04.png" alt="" /><br/> |
| --- | --- |
| **Transcoding (when streamer is uploading their video)**<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-07-03%20at%2016.42.29.png" alt="" /><br/> | **Upcoming**<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-07-03%20at%2016.44.47.png" alt="" /><br/> |

#### Size







**Jumbo**

<img src="https://docs-android.tokopedia.net/images/docs/res/Play-widget-jumbo.png" alt="" />





**Large**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-04-14%20at%2015.57.16-20230414-075721.png" alt="" />







**Small**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-04-14%20at%2015.58.31.png" alt="" />

**Medium**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-04-14%20at%2015.59.01.png" alt="" />

## Tech Stack

- <https://exoplayer.dev/> : Exo Player supports features that not currently supported by Android’s MediaPlayer API.
- [Coroutine](https://developer.android.com/kotlin/coroutines): To simplify code that executes asynchronously

## Navigation

The first step to getting started is to add a dependency in the `build.gradle` file of your module. The following will add a dependency to the Play Widget feature:



```
implementation project(rootProject.ext.features.playWidget)
```

### Hello World!

For sample implementation you can follow these steps:

- Switch to TestApp
- Choose `features/content/play_widget`
- Sync Gradle
- Open `MainActivity.kt`, and put:  
`startActivity(Intent(this, PlayWidgetSampleActivity::class.java))`
- and Run

You can also find current implementations on the Home, Shop, and Feed page.

## GQL List



| **Name** | **Tech Documentation** | **Usage** |
| --- | --- | --- |
| `playGetWidgetV2` | [[CVP][Viewer Experience] Get Play Widget V2](/wiki/spaces/CN/pages/800950288)  | Get play widget (used in Home, Shop, PDP, Disco, Feed) |
| `playGetContentSlot` | [[CVP][Viewer Experience] Get Content Slot](/wiki/spaces/CN/pages/798622188)  | Get play widget slot (used in User Profile, Video Tab - Feed, Explore Widget - Play) |

## How-to

If you want to take a glance about how to implement Play Widget you can go to this section:  

[Play Widget Sample](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#Play-Widget-Sample)

There are 2 ways to use Play Widget, based on whether you want to show Play Widget in a list or not:

- [Play Widget as RecyclerView.ViewHolder](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#Play-Widget-as-RecyclerView.ViewHolder)
- [Play Widget as Custom View](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#Play-Widget-as-Custom-View)
- [Play Widget as stand-alone card (for each item)](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#Play-Widget-as-stand-alone-card-(for-each-item)-new)

## Play Widget as RecyclerView.ViewHolder

If your page is based on a `RecyclerView` we recommend you to use `PlayWidgetViewHolder.kt`, because this class extends from `RecyclerView.ViewHolder`.



```
class MyAdapter(
  private val playWidgetCoordinator: PlayWidgetCoordinator
) : RecyclerView.Adapter<PlayWidgetViewHolder>() {
    
    private var mItemList: MutableList<PlayWidgetState> = mutableListOf()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayWidgetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(PlayWidgetViewHolder.layout, parent, false)
        return PlayWidgetViewHolder(itemView, playWidgetCoordinator)
    }

    override fun getItemCount(): Int = mItemList.size

    override fun onBindViewHolder(holder: PlayWidgetViewHolder, position: Int) {
        val item = mItemList[position]
        holder.bind(item)
    }
}
```

**Important!**

- Don’t need to create an item layout, use `PlayWidgetViewHolder.layout`
- Need to put a [PlayWidgetCoordinator](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/995465731/Play+Widget%3A+How+to+implement#Coordinator) in the constructor, click for a complete explanation.
- Call `holder.bind(PlayWidgetState)` on `onBindViewHolder(holder, position)`

If your module requires every ViewHolder to implement/extend from some sort of base class, we recommend you to create a new view holder that wraps `PlayWidgetViewHolder`. After wrapping `PlayWidgetViewHolder` with the newly created view holder, you need to use the `holder.bind(item: PlayWidgetState, holderWrapper: RecyclerView.ViewHolder)` to bind and pass your view holder instance (usually referred as `this`) as a param to `holderWrapper`. (For example, please look at `CarouselPlayCardViewHolder.kt` on feed module)

### 1. Data Layer (`PlayWidgetTools.kt`)

To get the data from our backend, we provide `PlayWidgetTools` that wraps all the use cases and mappers. List of available use cases:

- `PlayWidgetUseCase.kt`, get the widget data by `widgetType` (You have to implement your own type inside the `WidgetType` sealed class)
- `PlayWidgetReminderUseCase.kt`, to update the reminder status. Medium, large, and jumbo size widget have a reminder button for upcoming-type channels.

### 2. Mapper (`PlayWidgetMapper.kt`)

To easily map from network response to the UI model, you can use the built-in mapper from inside the `PlayWidgetTools` that starts with ``mapWidgetxxx``. For example, if you want to map the network response for showing the widget to the UI model you can call `mapWidgetToModel`.

But if somehow you don’t want to use the method from the `PlayWidgetTools`, you can use the standalone mapper `PlayWidgetUiMapper`.

### 3. Coordinator (`PlayWidgetCoordinator.kt`)

We told you before that you need a coordinator to be passed to the `PlayWidgetViewHolder`. This coordinator class is actually the main class that helps to control some of the main features on the widget. For now, it controls when to **auto-refresh** and **auto-play video** on the widget.

The constructor for `PlayWidgetCoordinator.kt` takes these parameters:

- LifecycleOwner
- Main Coroutine Dispatcher
- Work Coroutine Dispatcher
- `autoHandleLifecycleMethod` - This boolean indicates whether you want `PlayWidgetCoordinator` to automatically handles the **autoplay** based on the lifecycle owner that is passed.  
Default value is **true**.

If your page lifecycle is a simple page (no Tabs or ViewPager), then you can easily pass your Activity’s `lifecycleOwner` or Fragment’s `viewLifecycleOwner` to the coordinator. It will maintain the lifecycle of the widget by itself.

If your page uses ViewPager or tabs or some other complex mechanism, we suggest to set `autoHandleLifecycleMethod` param to false and handle the lifecycle by yourself. You can do this by calling coordinator’s `onPause()`, `onResume()`, and `onDestroy()` method by yourself (see [FeedPlusFragment](https://github.com/tokopedia/android-tokopedia-core/blob/release/features/content/feed/src/main/java/com/tokopedia/feedplus/view/fragment/FeedPlusFragment.kt) for example). Or if you are diligent enough, you can create your own implementation of LifecycleOwner that satisfies your needs.

There are currently 3 main functions in PlayWidgetCoordinator**:**

- `setListener(listener: PlayWidgetListener?)` is used to set a listener that can listen to when the widget is auto-refresh.
- **DEPRECATED: Use setAnalyticModel(model: PlayWidgetAnalyticModel)**  
`setAnalyticListener(listener: PlayWidgetAnalyticListener?)` is used to set a listener that can listen to when the analytic on the widget happened.
- `setAnalyticModel(model: PlayWidgetAnalyticModel)` is used to set analytic model. The tracker of Play Widget will be handled internally, you just need to provide the model of the analytic. The model should implements `PlayWidgetAnalyticModel` interface. Please create the model inside `com.tokopedia.play.widget.analytic.global.model`, as Play team will be the one who maintain it in the future. If you have any trouble or need some help, please contact `@android-minion-lance` on slack.
- `setImpressionHelper(helper: ImpressionHelper)`is used to set the logic for the impression. Sometimes the logic for impression is different between pages or tribe, that is why we make it configurable so you can determine your own impression logic.

**Important!** One thing to note is that a coordinator **is recommended** to be used only by 1 `PlayWidgetViewHolder`. For multiple `PlayWidgetViewHolder`, you can use multiple coordinator. We recommend this to prevent unwanted behavior.

### 4. Put everything together

As a good Android developer, you will most likely put the `PlayWidgetTools`inside your Presenter / ViewModel class. Then you connect the UI model that you have processed with the help of the tools to the ViewHolder in the UI. For easier development with a dagger, you just need to include `PlayWidgetModule.kt`in your existing dagger module or component.



```
@Module(includes = PlayWidgetModule::class)
class YourDaggerModule {
	
	@Provides
	fun providePlayWidget(playWidgetUseCase: PlayWidgetUseCase,
						mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>): PlayWidgetTools {
		return PlayWidgetTools(playWidgetUseCase, mapperProviders)
	}
}
```

Don’t forget to provide the ViewHolder with the **PlayWidgetCoordinator** as it will save you a LOT of time. We recommend you to create the coordinator inside your activity/fragment and then pass it to inside your adapter and your ViewHolder. 

## Play Widget as Custom View

If somehow your page is not a recycler-view based / your PO wants to put Play Widget outside of a RecyclerView, we already provided you the custom view one. The class name is `PlayWidgetView.kt`. Basically, this custom view is the same as the ViewHolder version but you need to connect this custom view to the coordinator by yourself.

Other than providing the Play Widget as custom view, we also allow you to use Play Widget Card View if it fits your needs. To make it easier to find the card, the name of the class follows this pattern:



```
PlayWidgetCard{Size}{Type}View
```

So, for example, if you want to find channel card of medium size, you can find it in `PlayWidgetCardMediumChannelView`.

<!--start status:GREEN-->NEW<!--end status--> **Play Widget with Grid Layout**.

You can customized Play Widget with Grid Layout through PlayLargeView. You can pass the field value in PlayWidgetChannelUiModel

`val gridType: PlayGridType = PlayGridType.Unknown`



```
enum class PlayGridType {
    Unknown,    Medium,    Large
}

```

## Play Widget as stand-alone card (for each item) <!--start status:PURPLE-->NEW<!--end status-->

You can use the stand alone card as your view holder layout.



```
<?xml version="1.0" encoding="utf-8"?>
<com.tokopedia.play.widget.ui.PlayWidgetLargeView 
xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
    
    
```

## Play Widget Sample

Sample implementation for Play Widget. 

As for data layer there are 2 options:

1. Data from GQL [[CVP][Viewer Experience] Get Play Widget V2](/wiki/spaces/CN/pages/800950288) is ready to use
2. Data from GQL [[CVP][Viewer Experience] Get Content Slot](/wiki/spaces/CN/pages/798622188) you need to map the data from GQL to UiModel on your own. Because the list is dynamic, and it depends how you’re gonna to show it.

Fyi, data layer is ready to use thru `PlayWidgetTools.kt` class. Please note as for fetching widgets it’s only handle GQL option 2. But you can use this class for turning on/off reminder toggle, deleting channel, updating views. Please refer it here how to use this class in your module <https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#4.-Put-everything-together> 



```
private fun processPlayWidget(isAutoRefresh: Boolean = false): <your UiModel> {
        val response = playWidgetTools.getWidgetFromNetwork( ⚠️ get widgets
            widgetType = PlayWidgetUseCase.WidgetType.Feeds, ⚠️ send widget type / source
            coroutineContext = dispatcher.io ⚠️ send coroutine ctx
        )
        val uiModel = playWidgetTools.mapWidgetToModel(response) ⚠️ map data from GQL to Ui Model
        return <your UiModel>
    }
```

In UI layer, if you use this approach <https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#Play-Widget-as-RecyclerView.ViewHolder> you can use this coordinator <https://tokopedia.atlassian.net/wiki/spaces/PA/pages/989043703/Play+Widget#3.-Coordinator-(PlayWidgetCoordinator.kt)> else you need to custom your own coordinator to pass the value.

In your fragment:



```
val coordinatorMap = List(size) {
            PlayWidgetCoordinator(this, autoHandleLifecycleMethod = false).apply {
                setAnalyticListener(PlayWidgetSampleAnalytic(requireContext()))
            }
        }.associateWith { null } ⚠️ each ViewHolder has one coordinator

        adapter = PlayWidgetSampleAdapter(coordinatorMap) ⚠️ pass coordinator in yout adapter
```

Done, Play Widget is ready to use! 



---

## Controlling the Widget

### 1. PlayWidgetViewHolder

Have been handled automatically. Ready to use, no need to configure anything. You can use this if you have several widget in one page so it could be dynamic.

### 2. Custom View (ex. PlayLargeView, PlayMediumView, etc)

If you want to use specific widget alone, no dynamic widget(s) you can use this.

To let the coordinator know that this is the widget that you want to control, you need to call `controlWidget(widget: PlayWidgetView)`. By doing this all the listeners that you have set to the coordinator can be automatically assigned to the custom view.

To connect the UI Model to the widget and to let the coordinator know about the model, you need to call `connect(widget: PlayWidgetView, state: PlayWidgetState)`.By doing this, the widget will also show the new data from the model.

### 3. Card-level (ex. PlayWidgetCardChannelMediumView, etc)

If you want to use only the card, you can make your own list and use the card only. Please remember that you need to configure the autoplay, auto-refresh on your own because it’s not attached in this type.

### Auto-Refresh

If you use the ViewHolder version, it is automatically handled for you.

If you use the custom view and have called `controlWidget` and `connect` function on the coordinator, auto-refresh is already good to go and you only need to listen to the auto refresh listener.

### Auto-Play

If you use the ViewHolder version, it is automatically handled for you.

If you use the custom view and have called `controlWidget` and `connect` function on the coordinator, autoplay is already good to go. Or you can custom.

### Update Total View from Channel Room

You need to handle this one manually by listening to when the channel is clicked. When the channel is clicked, you will get the appLink. You need to open the page with that appLink and also pass a request code.

After that, you need to listen to that request code from your Activity or Fragment’s onActivityResult. There you can get 2 data:

- `EXTRA_CHANNEL_ID` -> Get the channel id of the opened channel room
- `EXTRA_TOTAL_VIEW` -> Get the last total view of the opened channel room

With these two info, you can then update the widget (either by `diffutil` or any of your methods) accordingly. (We recommend you to update it via your Presenter or ViewModel so that you have one source of truth for data)

### Set Toggle Reminder for Upcoming Channel

Medium size widget has a reminder button for upcoming-type channels, therefore we provide a use case and mapper to update the reminder status inside `PlayWidgetTools`**.**

Implement your custom analytic in play widget. (Don’t forget to create Analytic Validator for your analytic).

## Global Analytic

Since version `MA-xxx` and `SA-xxx`, all analytic related to Play Widget will be managed by Play team. All you need to do as Play Widget implementer is to talk to our Data Analyst ([Christopher Aryo Pambudi](https://tokopedia.atlassian.net/wiki/people/6163fb22048360006a9da289?ref=confluence) or [Farhan Reynaldo Hutabarat (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/619adf076d002b006b56b5ba?ref=confluence)) and discuss certain prefix that is needed to identify your page.

After that, you can create your own `PlayWidgetAnalyticModel` and put in `com.tokopedia.play.widget.analytic.global.model` or ask for help from Android Play team.



---

## Action Items

- Remove duplicate file and/or unused resources

## Useful Links

###### *Put reference links that you have in this section. You can attach a Figma URL, Thanos URL, etc.*

- [Play's widget Template & Usage](/wiki/spaces/CN/pages/787091549)
- <https://www.figma.com/file/4NLsrh1meiVpoXa4fFY39P/%5BUI-M-Play%5D-Play-New-Widget?node-id=1%3A2480>
- <https://github.com/tokopedia/gqlserver/blob/staging/gql/play/README.md>
- [AN-20784](https://tokopedia.atlassian.net/browse/AN-20784)
 -
 Getting issue details...

STATUS
- Global Analytics Thanos

## FAQ

###### *Any common questions that you couldn’t put in those sections above, you can put them in this FAQ section.*

<!--start expand:Where can I request a sourceId?-->
You can request on the [media slack channel](https://slack.com/)
<!--end expand-->



---




