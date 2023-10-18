---
title: Tipping Gojek
labels:
- android
- logistic
- tipping-gojek
- tracking-page
---

<!--left header table-->
| **Status**      | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                          |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors    | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) |
| Product Manager | [Septian Bayu Laksono](https://tokopedia.atlassian.net/wiki/people/5df8541fa0602c0cabdce844?ref=confluence)                                                                                                                                                                                                |
| Team            | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Module type     | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                         |
| Product PRD     | [PRD Tipping To Gojek Driver](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1606320593/Tipping+to+Gojek+Driver)                                                                                                                                                                                     |
| Module Location | `features/logistic/logisticorder`                                                                                                                                                                                                                                                                          |

<!--toc-->

## Overview

### Background

For Tokopedia on-demand orders (Gojek/Grab Order), there is a habit where our buyer wants to give tips to the Gojek/Grab Driver. The buyer gives tips when the driver asks about parking fees or the buyer generously wants to give tips. 

But currently, there is no seamless flow for buyers to give tips on Tokopedia App. Buyers need to give tips via cash. This pain point is frustrating for buyers where buyers are not at home, buyers living at apartments (need to go down to the lobby). And it gets worse when right now during a pandemic buyer does not want to meet the driver directly to hand over tips via cash. 

### Project Description

Tokopedia wants to provide a seamless flow for buyers to give cashless tips to drivers via Tokopedia App. With this product, we will solve buyer pain points where the buyer does not need to meet the driver and give cash tips. 

### UI Page

![](../res/tippinggojek/Screenshot%202023-02-10%20at%2015.14.53.png)

![](../res/tippinggojek/3PL%20Failed%20Pickup%20-%20Lacak%20%282%29.png)

## Navigation

part of [tracking page](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1843134919/Tracking+Page)

Tipping Gojek → show when ordered with Gojek driver

Swift Driver Info → shown when user buy tokopedia now! product and the assigned driver is from swift



## GQL List



| **GQL & API Name** | **Documentation Link** | **Description** |
| --- | --- | --- |
| `Get Driver Tips Info` | [Get Driver Tips Info](https://tokopedia.atlassian.net/wiki/spaces/LG/pages/1753187034/Get+Driver+Tips+Info)  | To get tipping detail info |



---

## Useful Links

- [Figma](https://www.figma.com/file/13FQWDiEYpWMMAsOrqQQ8X/Tipping-to-Gojek-%26-Grab-Driver?node-id=1383%3A98393)