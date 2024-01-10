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

Currently from Backend side have
the [Ticker Unification](https://docs.google.com/presentation/d/1hWc2LN2zxWEOPUJb8vnfzgLmmQ-FJYt3eczuf69CNPU/edit#slide=id.p3)
feature that can accommodate the whole ticker in Tokopedia service.
To facilitate this, we've developed a Targeted Ticker Widget to facilitate Ticker Unification. with this widget, effectively
reducing the code required for implementing the UI and create one use case can be use by the team.

## **Overview**

### Targeted Ticker Widget

Targeted Ticker Widget is a widget component that used to centralize usage of Targeted Ticker provide from Backend side.

This Targeted Ticker Widget is extending our ticker. so its will work as usual ticker but with build-in useCase.

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

for implement this, you will require 3 params for targeted ticker.

Please note, The data will be from backend, in your side need mapping to model Targeted Ticker Widget

| **Variable Name** | **Type**       | **Description**                   |
|-------------------|----------------|-----------------------------------|
| `page`            | `String`       | Page Name                         |
| `template`        | `Template`     | To show template data             |
| `target`          | `List<Target>` | To show dynamic data from backend |

#### 3. Custom Targeted Ticker widget with Ticker from backend

to custom targeted ticker widget ui, we can use funtion setResultListener and custom ticker inside of it

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

for more custom, you can use this usecase.

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

#### 5. References

Currently we use Targeted Ticker widget on this page. You can use as references

1. ManageAddressFragment
2. TrackingPageFragment
3. RequestPickupFragment
4. ConfirmShippingFragment

## Useful Links

- [Backend Doc - Ticker Unification - Auto Ticker](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/2436301320/Ticker+Unification+-+AutoTicker)
- [Backend Doc - Ticker Unification - Dynamic Ticker Content](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/2443968897/Ticker+Unification+-+Dynamic+Ticker+Content)
- [GQL Targeted Ticker](https://tokopedia.atlassian.net/wiki/spaces/EI/pages/1919520722/GQL+-+Get+Targeted+Ticker)