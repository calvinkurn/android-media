---
title: Logistic Common
labels:
- logistic
- edit-address
- common
---
<!--left header table-->
| **Status**      | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                          |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors    | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Module type     | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                                                                                                                                                                                                                |
| Module Location | `features/logistic/logisticCommon`                                                                                                                                                                                                                                                                         |

<!--toc-->

## Overview

Logistic Common module is a common library module containing ui, usecase, and utils related to logistic.

## How-to

### Require

Make sure your module depends on `logistic_common` module. If not, then put this line in your module `build.gradle` file



```
implementation projectOrAar(rootProject.ext.features.logisticCommon)
```

### UI

#### DelayedEtaBottomSheetFragment

Use to show bottom sheet with information on changes in estimated delivery orders.

![](res/delayed_eta_bottom_sheet.png)

<!--start expand:newInstance()-->
call the fragment 

`DelayedEtaBottomSheetFragment.newInstance()`

param 



| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `delayedInfo` | `String` | Delayed information  |
<!--end expand-->

###### related doc : [figma Dynamic eta](https://www.figma.com/file/Ca0Lakjx4tZDf4gvClXkQ3/%5BUI-%2F-UX---D%2FM%5D----Dynamic-ETA-(Estimated-Team-Arrival)?node-id=341%3A13941)

### Use case

#### EditAddressUseCase

this use case can be use to edit used address. including update when there is no pinpoint. current use in checkout, occ, tokofood

<!--start expand:Params-->
| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `EditAddressParam.ADDRESS_ID` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.ADDRESS_NAME` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.ADDRESS_STREET` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.POSTAL_CODE` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.DISTRICT_ID` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.CITY_ID` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.PROVINCE_ID` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.LATITUDE` | `String` | for update pinpoint |
| `EditAddressParam.LONGITUDE` | `String` | for update pinpoint |
| `EditAddressParam.RECEIVER_NAME` | `String` | if just update pinpoint only, mandatory to add this param as well |
| `EditAddressParam.RECEIVER_PHONE]` | `String` | if just update pinpoint only, mandatory to add this param as well |
<!--end expand-->

###### related doc : [Add Pinpoint on Existing Address](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2052849693/Add+Pinpoint+on+Existing+Address)

#### EligibleForAddressUseCase <!--start status:RED-->REMOVE LATER<!--end status-->

###### related doc : [[Tech Debt Logistic ] - Optimize Add Address](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2165244783/Tech+Debt+Logistic+-+Optimize+Add+Address)

#### GetAddressCornerUseCase

This use case

 use for get list address user address or list corner address in Tokopedia. in this use case, can use two function

 `execute()` and `loadMore()`. execute for get the first page of list address and loadMore to get rest of list address.

<!--start expand:param-->
| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `query` | `String` | query for search address. give empty string (““) to get all addresss |
| `prevState` | `Int?` |   |
| `localChosenAddrId` | `Long?` |   |
| `isWhitelistChosenAddress` | `Boolean` |   |
| `excludeSharedAddress` | `Boolean`  | default is false |
<!--end expand-->

###### related doc : [Manage Address](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1833893904/Manage+Address) , [Get Address Corner - /maps/v1/address/corner](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/694794650/Get+Address+Corner+-+maps+v1+address+corner)

#### CustomProductLogisticUseCase

This use case for get list of available courier that can be activated by seller for specific product.

<!--start expand:getParam()-->
this funtion to get param by send shop id, productId, and cplParam



| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `shopId` | `Long` |   |
| `productId` | `Long?` |   |
| `cplParam` | `List<Long>?` |   |
<!--end expand-->

<!--start expand:execute()-->
to get list of courier by send `CPLParam` was just get from `getParam()`



| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `params` | `CPLParam` |   |
<!--end expand-->

###### related doc : [Custom Product Logistic](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2103097544/Custom+Product+Logistic)

#### RevGeocodeUseCase

Reverse Geocode Use Case use to get data address detail from lat long.

###### related doc : [GMaps Geocode - /maps/geocode/ajax](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/586711733/GMaps+Geocode+-+maps+geocode+ajax)

#### ShopMultilocWhitelistUseCase

how to use : [Eligible Multilocation](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1995178121/Eligible+Multilocation) 

### Utils

#### MapsAvailabilityHelper

helper class for detect device google map service

<!--start expand:isMapsAvailable()-->
param `context : Context`

return true when google map available
<!--end expand-->

###### related doc : [Use Designated Page When Pinpoint](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2089192790/Use+Designated+Page+When+Pinpoint?NO_SSR=1)

#### LogisticUserConsentHelper

 helper to show button user consent. this view also contain listener to route user.

![](res/logistic_user_consent_button.png)

<!--start expand:displayUserConsent()-->
to use this object call `LogisticUserConsentHelper.displayUserConsent()`

Param



| **Name** | **Type** | **Notes** |
| --- | --- | --- |
| `context` | `Context` | context |
| `userId` | `String` | id user for analytics |
| `textView` | `TextView` | textview to place the text as placeholder |
| `buttonText` | `String` | text button |
| `screenName` | `String`  | screenName for analytics |
<!--end expand-->

###### related doc : [User Concent Information Figma](https://www.figma.com/file/KZfYDzvWP3nm9smBdvxYFu/Address-v3.3.1---User-Consent-Information?node-id=98-330&t=XOwNvvF8Yc6xyCkc-0)

#### LogisticImageDeliveryHelper

helper to get proof of delivery image. current use by tracking page and return to shipper

how to use : [Proof of Delivery (POD)](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1967065652) 

###### related doc : [Return to Shipper](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2107638120/Logistic+Seller+Return+To+Shipper)



---

## Action Items

- need to make sure
- did we still use `TrackingPageRepository` inside `logisticCommon` since we have `TrackingPageRepository` inside `logisticorder`
- did we still need `OrderCourierRepository` inside `logisticCommon`
- did we still need `InsuranceTnCRepository` inside `logisticCommon`

- Update this doc related to [Optimize Add Address](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2165244783/Tech+Debt+Logistic+-+Optimize+Add+Address).
- update our current `repository` to `usecase`(need to check the prio and impact)
- check `CustomProductLogisticUseCase` why its in `repository` package