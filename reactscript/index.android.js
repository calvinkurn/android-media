'use strict';

import React, { Component } from 'react';
import codePush from "react-native-code-push";
import {
  AppRegistry,
  StyleSheet,
  Text,
  AppState,
  AsyncStorage,
  View
} from 'react-native';
// import { HotList_ } from './src/configs/router';
import OfficialStore from './src/pages/official-store/setup'


// let codePushOptions = { checkFrequency: codePush.CheckFrequency.ON_APP_RESUME };

class Home extends Component {
  render(){
    if(this.props.Screen === 'official-store'){
      return <OfficialStore />
    }
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

// Home = codePush({ checkFrequency: codePush.CheckFrequency.ON_APP_RESUME, installMode: codePush.InstallMode.ON_NEXT_RESUME })(Home);
module.exports = Home;
AppRegistry.registerComponent('MAIN', () => Home);

