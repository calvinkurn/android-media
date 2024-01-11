---
span
---
<!--left header table-->

| Status          | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                          |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors    | [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Module type     | <!--start status:Grey-->SUB-FEATURE<!--end status-->                                                                                                                                                                                                                                                       |
| Module Location | `features/logistic/targetedticker`                                                                                                                                                                                                                                                                         |

<!--toc-->

## Background

Currently, the backend side has
the [Ticker Unification](https://docs.google.com/presentation/d/1hWc2LN2zxWEOPUJb8vnfzgLmmQ-FJYt3eczuf69CNPU/edit#slide=id.p3)
feature that can accommodate the whole ticker in the Tokopedia service. To enhance efficiency, we've developed a Targeted Ticker
widget to facilitate ticker unification. With this widget, effectively reducing the code required for implementing the UI and
creating one use case can be used by the team.

## **Overview**

### Targeted Ticker Widget

Targeted Ticker Widget is a component designed to centralize the utilization of targeted ticker data provided from the backend.

The Targeted Ticker Widget extends our standard ticker functionality, operating like a regular ticker but with a built-in use
case.

![Screenshot 2024-01-10 at 09.02.10.png](..%2F..%2F..%2F..%2F..%2F..%2F..%2FDesktop%2FScreenshot%202024-01-10%20at%2009.02.10.png)

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

Example :

```
loadAndShow(param)
```

this is case to use

##### Case 1 : Hardcoded Key

this case when your page show simple targeted ticker without need any data from backend side.

Example :

```
 loadAndShow(page = TargetedTickerPage.ADDRESS_LIST_NON_OCC)
```

---

##### Case 2 : Data Passing from Backend

On the Android side, you will need to map data from the backend. 

Example :

```
 // get from your backend map to Targeted Ticker Model
 val template = ....
 val target = ....
 val page = ....

 // adjust the model to pass the data
 val param = TargetedTickerParamModel(
    page = page,
    template = template,
    target = target
 )
 
 //load and show your targeted ticker
 loadAndShow(param)
```

Param / data you required to passing

| **Variable Name** | **Type**       | **Description**                                                                   |
|-------------------|----------------|-----------------------------------------------------------------------------------|
| `page`            | `String`       | Page Name, it can be provide from backend or hardcode                             |
| `template`        | `Template`     | This will provide from your Backend, please ask your backend                      |
| `target`          | `List<Target>` | This will provide from your Backend, please ask your backend. its can be optional |

Note :
1. Target required from the backend Targeted ticker as logic when to show ticker
2. Template required from the Backend Targeted Ticker as logic for replaceable key value

#### 3. Custom Targeted Ticker widget with Ticker from backend

To create a custom targeted ticker widget UI, we can use the function setResultListener and a custom ticker inside of it.

```
setResultListener(
    onSuccess = { ticker ->
        ...
    },
    onError = {
        ....
    }
)
```

#### 4. UseCase only

For more customization, you can use GetTargetedTickerUseCase .

Example :

```
launch {
        try {
            val response = getTargetedTickerUseCase(TargetedTickerParamModel(page = YOUR_PAGE_NAME_STRING))
           
            ......
        } catch ( e: Throwable) {
            ....
        }
    }
```

## Useful Links

- [Backend Doc - Ticker Unification - Auto Ticker](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/2436301320/Ticker+Unification+-+AutoTicker)
- [Backend Doc - Ticker Unification - Dynamic Ticker Content](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/2443968897/Ticker+Unification+-+Dynamic+Ticker+Content)
- [GQL Targeted Ticker](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/1919520722/GQL+-+Get+Targeted+Ticker)