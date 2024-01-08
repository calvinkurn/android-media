---
span
---
<!--left header table-->


| Status          | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                          |
| --------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Contributors    | [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Module type     | <!--start status:Grey-->SUB-FEATURE<!--end status-->                                                                                                                                                                                                                                                       |
| Module Location | `features/logistic/targetedticker`                                                                                                                                                                                                                                                                         |

<!--toc-->

## Background

## **Overview**

### Targeted Ticker Widget

Targeted Ticker Widget is a widget component that used to centralize using Targeted Ticker provide from Backend side.

## Flow of Component Usage

## Variants of C

# General Component Usage

# Setup

#### 1. Add dependencies

Add `targetedticker` dependency to your module's build.gradle

```
implementation projectOrAar(rootProject.ext.features.targetedticker)
```



#### 2. Implement Targeted Ticker Widget

Add TargetedTicker Widget to your fragment’s layout

``` 
<com.tokopedia.targetedticker.ui.TargetedTickerWidget
      android:id="@+id/ticker_manage_address"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />
```


to get targetedTicker data from backend side, you need to call function loadAndShow()

this is example to call it

```
loadAndShow(param)
```
this is case to use

##### Case 1 : Hardcoded Key
this case when your page show simple targeted ticker without need any data from backend side. 

this is example call from manage address 
```
 loadAndShow(page = TargetedTickerPage.ADDRESS_LIST_NON_OCC)
```
page => will act as key for the page.

---

##### Case 2 : Data Passing from Backend
this case will require data from your backend provide the data and will be passing to Targeted Ticker Widget

example of usage 
```
 val param = TargetedTickerParamModel(
        page = TargetedTickerPage.REQUEST_PICKUP,
        targets = confirmReqPickupResponse.dataSuccess.tickerUnificationTargets.map {
            TargetedTickerParamModel.Target(it.type, it.values)
        }
 )
 
 loadAndShow(param)
```

## Useful Links

- https://docs.google.com/presentation/d/1hWc2LN2zxWEOPUJb8vnfzgLmmQ-FJYt3eczuf69CNPU/edit#slide=id.g2536b5933d2_0_6
  https://tokopedia.atlassian.net/wiki/spaces/EI/pages/1919520722/GQL+-+Get+Targeted+Ticker
- [Figma Dynamic ETA](https://www.figma.com/file/Ca0Lakjx4tZDf4gvClXkQ3/Dynamic-ETA-v1.0---%5BM%2FD%5D?t=c19BCJRsa9gQhYF9-6)
- [Figma Add copy paste button and Empty Field state](https://www.figma.com/file/tNgEL1SLOrz4hKsk9p0jT3/%5BUIUX-M%2FD%5D-Tracking-%2F-Lacak-Master?node-id=203%3A9724)
