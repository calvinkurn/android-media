'use strict';

import React, { Component } from 'react';
import codePush from "react-native-code-push";
import {
  AppRegistry,
  StyleSheet,
  Text,
  ActivityIndicator,
  AppState,
  View
} from 'react-native';
import { NavigationModule, NetworkModule } from 'NativeModules';
import { HotList_ } from './src/configs/router';
import OfficialStore from './src/pages/official-store/setup'


let codePushOptions = { checkFrequency: codePush.CheckFrequency.ON_APP_RESUME };

class Home extends Component {
  state = {
    appState: AppState.currentState
  }

  componentWillMount() { 
    console.log(this.props)
    AppState.addEventListener('change', this._handleAppStateChange);
  }

  componentWillUnmount() {
    AppState.removeEventListener('change', this._handleAppStateChange);
  }


  _handleAppStateChange = (nextAppState) => {
    if (this.state.appState.match(/inactive|background/) && nextAppState === 'active') {
      console.log('App has come to the foreground!')
    }
    this.setState({appState: nextAppState});
  }

  render(){
    if (this.props.Screen == 'HotList'){
      return <HotList_ />
    } else if (this.props.Screen == 'official-store' && this.state.appState == 'active'){
      return <OfficialStore screenProps={{ User_ID: this.props.User_ID, appState: 'active' }}  />
    } else {
      return(
        <View style={{justifyContent:'center', alignItems:'center', flex:1}}>
          <ActivityIndicator size="large" />
        </View>
      )
    }
  }
}
var styles = StyleSheet.create({
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

Home = codePush({ checkFrequency: codePush.CheckFrequency.ON_APP_RESUME, installMode: codePush.InstallMode.ON_NEXT_RESUME })(Home);
module.exports = Home;
AppRegistry.registerComponent('MAIN', () => Home);

