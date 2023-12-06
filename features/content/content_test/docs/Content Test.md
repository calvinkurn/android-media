---
title: "Content Test"
labels:
- testing
- content
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| **Contributors** | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Meyta Taliti (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 16 May 2022 / <!--start status:BLUE-->MA-3.174<!--end status--> <!--start status:BLUE-->SA-2.104<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Module Location | `libraries.contentTest` | `libraries/content_test` |

<!--toc-->

## Release Notes

<!--start expand:1 Nov 2022 (MA-3.198 & SA-2.128)-->
Renamed to `content_test`

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/29707>
<!--end expand-->

<!--start expand:16 May 2022 (MA-3.174 & SA-2.104)-->
Released as `play_test`

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/26065>
<!--end expand-->

## Overview

**Content Test** is an internal library (for content team) that helps developers in implementing test (Unit Test or UI Test). This library is created for the purpose of eliminating boilerplate that developers usually write when they created a test.

### Background

As a developer, we are required (or at least encouraged) to create test for every features that we develop. There are some tests that we can do as a developer, two of which are Unit Test and UI Test.

In Android, Unit Test can be done with the help of JUnit4 library and any matchers library. Whereas in UI Test, although there are many libraries that can be used to do this, the basic library that we usually use is Espresso. 

Although these libraries provide us with many functions that can be used immediately, sometimes there are some complicated things that we want to do in our tests where we need to create custom functions. These functions might be used in multiple features or modules and if we create this function over and over again, it will become a boilerplate.

### Project Description

Content Test is a library that allows developers from Content Tribe to use functions that are often used when they are doing Unit Test or UI Test that are not available by default from the original test library.

This library provides helper functions for creating Cassava Test, Espresso UI Test, and Unit Test.

## Tech Stack

- [**Espresso**](https://developer.android.com/training/testing/espresso): Default UI Test library for Android that can assert many states of Android views.