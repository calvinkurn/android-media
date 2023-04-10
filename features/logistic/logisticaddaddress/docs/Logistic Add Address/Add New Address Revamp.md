---
title: Add New Address Revamp
labels:
---


| **Status** | ​<!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Ivan Fadhila (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5dd7516d58fc78100710fcea?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Product Manager | [Nuringtyas Rahwinarni](https://tokopedia.atlassian.net/wiki/people/5f58b98ed2c77e0075ac9865?ref=confluence)  |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051) |
| Release date | ​28 Jan 2022 / ​<!--start status:GREY-->MA-3.161<!--end status-->  |
| Module type | ​<!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | [Add Address Revamp](https://docs.google.com/document/d/1FlZzJYqRsFuuJf-_jZdUQ3T1U3mipIehQ3Zvy8dEJfQ/edit#heading=h.6z6rqhcm3kop)  |
| Module Location | `features/logistic/logisticaddress` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:28 Jan 2022 (MA-3.161)-->
[Release Add New Address Revamp](https://docs.google.com/document/d/1FlZzJYqRsFuuJf-_jZdUQ3T1U3mipIehQ3Zvy8dEJfQ/edit#)
<!--end expand-->

<!--start expand:10 Feb 2023 (MA-3.208) (SA-2.138)-->
[Edit Journey For Huawei Device](https://docs.google.com/document/d/156V7TudzmolCvwQ8MQGoWOTDaHuHGSHxRVu-HqNQ7_E/edit?pli=1#heading=h.w51s9gpss7d9)
<!--end expand-->

## Overview

### Background

Address is having an important role in user experience of PG transactions in tokopedia. It will be needed in order to complete the checkout process. Address creation flow should be frictionless experience for users.

**ANA** (Add New Address) **Revamp**, is the new ANA, the purpose of **ANA Revamp** is to make address creation flow become more frictionless. It also solve some of user pain-points, where user can’t find their exact address in Pinpoint Page. Currently ana revamp already rolled out to 100% of user (to check user is eligible ana revamp from [[GQL] Is Eligible For Address Feature](/wiki/spaces/LG/pages/1416832716) )

### Project Description

This project is used to add address, there is a positive flow & negative flow when the user will add an address. Positive flow is a condition where the user add an address with a pinpoint. Negative flow is a condition where the user add an address without a pinpoint.

## Navigation

For detail about navigation can see this [figma](https://www.figma.com/file/e3ygzrs5pBsUuvKRPnHdoV/Address-v3.1---ANA-Revamp-%5BUX%5D?node-id=247%3A7813&t=ZipAkximkWfGD4em-0)

**Positive Flow**

![](../res/addnewaddress/Screen%20Shot%202023-02-16%20at%2009.21.11.png)  
**Negative Flow**

![](../res/addnewaddress/Screen%20Shot%202023-02-16%20at%2009.22.23.png)## Tech Stack

- MVVM
- ViewBinding
- JUnit (Unit Test)
- Espresso (UI Test)

## Flow Diagram

![](../res/addnewaddress/Screen%20Shot%202021-04-22%20at%2008.23.24.png)## How-to

To go to the add new address revamp, we can use the applink below



```
RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
```

- applink  
`tokopedia-android-internal://logistic/addaddress/v3/`

### GQL List



| **GQL Name** | **Documentation Link** | **Description** |
| --- | --- | --- |
| `KeroMapsAutoComplete` | [GMaps Autocomplete - /maps/autocomplete](/wiki/spaces/LG/pages/586482573)  | To get all available location using keyword |
| `keroMapsAutofill` | [GMaps Autofill District Geocode - /maps/geocode/get-district](/wiki/spaces/LG/pages/694818899)  | To get district detail from latlong |
| `KeroPlacesGetDistrict` | [GMaps Autofill District Place Detail - /maps/places/get-district](/wiki/spaces/LG/pages/694750060)  | To get district detail from placeId |
| `keroGetDistrictBoundaryArray` | [District Boundary - /v2/district-boundary/get](/wiki/spaces/LG/pages/586909442)  | To get boundary coordinate of `district_id` |
| `kero_addr_get_district_center` | [Get District Center Geolocation & Geocell - /v1/district/geocell](/wiki/spaces/LG/pages/1811317845)  | To get latitude & longitude of district center from `district_id` |
| `KeroAddrGetDefaultAddress` | [[GQL] Get Default Address](/wiki/spaces/LG/pages/1548849560)  | To get the default/primary address of a user. |
| `KeroAddAddress` | [Add Address - /maps/v1/address/add](/wiki/spaces/LG/pages/571965659)  | To add new address |

## Action Items

- Optimize code on Add Address V3
- Remove Add Address V2 (Add New Address)

## Useful Links

- Design : [Add Address Revamp](https://www.figma.com/file/8JT5Va3Bxgk2fAiX0pAn3y/Address-v3.3---Share-Address?node-id=732%3A85129&t=BsNPGPAHJMDhxXyo-0)
- GTM : [Add Address Revamp](https://mynakama.tokopedia.com/datatracker/requestdetail/view/1137)
- [Entry Point : Add & Edit Address (Source Param)](/wiki/spaces/PA/pages/2034631658)
- [Add Pinpoint on Existing Address](/wiki/spaces/PA/pages/2052849693/Add+Pinpoint+on+Existing+Address)
- [Hide Pinpoint Page Entry Point](/wiki/spaces/PA/pages/2092339526/Hide+Pinpoint+Page+Entry+Point)