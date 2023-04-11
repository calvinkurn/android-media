---
title: Localization Choose Address Common
labels:
- logistic
- localization-choose-address
- common
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | Android: [Angga Prasetiyo](https://tokopedia.atlassian.net/wiki/people/5c90710f9701df2d3a08e7c1?ref=confluence) [Ivan Fadhila (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5dd7516d58fc78100710fcea?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Felicia Widjaja](https://tokopedia.atlassian.net/wiki/people/5c90c19d196abe2e884c82ed?ref=confluence)  <br/>Backend Engineer: [Joel Hutasoit](https://tokopedia.atlassian.net/wiki/people/5cabff78faab36165eb9d1e7?ref=confluence) |
| Product Manager | [Nuringtyas Rahwinarni](https://tokopedia.atlassian.net/wiki/people/5f58b98ed2c77e0075ac9865?ref=confluence) |
| Team | Minion Bob |
| Release date | March 2021 |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status--> |
| Product PRD | [https://docs.google.com/document/d/1wYVu25dTo8YsQ7s3ws8gq37T85KWPuE9siNjkLtVoGU/edit#heading=h.rq438b6zh7yv - **Connect to preview**](https://docs.google.com/document/d/1wYVu25dTo8YsQ7s3ws8gq37T85KWPuE9siNjkLtVoGU/edit#heading=h.rq438b6zh7yv) |
| Module Location | `features/logistic/localizationchooseaddress_common` |

<!--toc-->

## Overview

This common module consists of:

- `ChosenAddress`
- `ChosenAddressRequestHelper`: convert local chosen address (`LocalCacheModel`) to GQL param

Sample usage can be seen in `GetMiniCartListUseCase.kt`

Add this module as dependency if your module **only need address data as parameter to GQL call** (not the whole widget UI). 

Refer to this [documentation](/wiki/spaces/PA/pages/1157243000/Choose+Address+Widget+-+Localized+Experience) if your module also need the Choose Address Widget.

## How to

1. add dependency in the `build.gradle` file of your module.



```
implementation project(rootProject.ext.features.localizationchooseaddress_common)
```

2. call `ChosenAddressRequestHelper.getChosenAddress()` to return `ChosenAddress` model that consists of:



| **Variable** | **Type** | **Description** | **Example** | **Status** |
| --- | --- | --- | --- | --- |
| mode | Int | Flag to define current state of local chosen address data: | `MODE_EMPTY = 0` means device doesn’t have any local chosen address data saved`MODE_ADDRESS = 1` means user already selected address`MODE_SNIPPET = 2` means user is using snippet location as address |  |
| address\_id | String | User chosen address' ID. Use this field to determine whether:- User logged in/not<br/>- User already selected any saved address<br/> |   | <!--start status:GREEN-->RELEASE<!--end status--> |
| district\_id | String | User chosen address district id |  |  |
| geolocation | String | User chosen address latitude and longitude.  |   | <!--start status:GREEN-->RELEASE<!--end status--> |
| postal\_code | String | User chosen address postal code |   | <!--start status:GREEN-->RELEASE<!--end status--> |
| tokonow | ChosenAddressTokonow | NOW! related data |  | <!--start status:GREEN-->RELEASE<!--end status--> |

**ChosenAddressTokonow**



| **Variable** | **Type** | **Description** | **Example** | **Status** |
| --- | --- | --- | --- | --- |
| shop\_id | String |   |   | <!--start status:GREEN-->RELEASE<!--end status--> |
| warehouse\_id | String | NOW! warehouse nearest user’s chosen address. |   | <!--start status:GREEN-->RELEASE<!--end status--> |
| service\_type | String | NOW! service based on user preference: | - 2hr<br/>- 15m<br/> | <!--start status:GREEN-->RELEASE<!--end status--> |
| warehouses | List<LocalWarehouseModel> | NOW! nearest warehouse**s** based on each service type- Size of the list will be the same with number of `service_type` NOW! serves. Currently there are only 2.<br/>- If one of the service type is not available for user’s chosen address, the `warehouse_id` will be zero<br/> | - User eligible for 2h & 15m service type: `[ {service_type:"2h", warehouse_id:12},{service_type:"15m", warehouse_id:23} ]`<br/>- User eligible only for 2h service type `[ {service_type:"2h", warehouse_id:12},{service_type:"15m", warehouse_id:0} ]`<br/>- User not eligible for NOW! `[ {service_type:"2h", warehouse_id: 0},{service_type:"15m", warehouse_id: 0} ]`<br/> | <!--start status:GREEN-->RELEASE<!--end status--> |

## Useful Link

- [Choose Address Widget - Localized Experience](/wiki/spaces/PA/pages/1157243000/Choose+Address+Widget+-+Localized+Experience)