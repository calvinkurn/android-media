import React, { Component } from 'react'
import {
  StyleSheet,
  // Text,
  View,
  // DrawerLayoutAndroid,
  // ToolbarAndroid,
  // Dimensions,
  // TextInput,
  // TouchableNativeFeedback,
  // TouchableWithoutFeedback
} from 'react-native'
import VisibleProductList from '../../../containers/VisibleProductList'
import Ticker from '../product/Ticker'
import CartContainer from '../../../containers/CartContainer'
import SearchContainer from '../../../containers/SearchContainer'
import { reloadState } from '../../../actions/index'
// import PasswordPopup from './PasswordPopup'
import { connect } from 'react-redux'


class POS extends Component {
  constructor() {
    super()
    this.state = {
      cartOpen: false,
      searchOpen: false
    }
  }


  componentDidMount(){
    // Todo List:
    // 1. When user checkout, send event emitter to clear state but Keep the carts

    // this.props.dispatch(GetPaymentRate())
    // console.log(this.props)
    this.props.dispatch(reloadState('POS'))
  }



  onActionSelected = (position) => {
    if (position === 1) {
      console.log('onActionSelected called')
      this.setState({
        cartOpen: true
      })
    } else {
      this.setState({
        searchOpen: true
      })
    }
  }

  onIconClick = () => {
    this.drawerPane.openDrawer()
  }

  render() {
    return (
        <View style={styles.container}>
          <Ticker />
          <VisibleProductList />
          {/* <PasswordPopup
            navigation={this.props.navigation}
            ref={(passwordPopup) => { this.passwordPopup = passwordPopup; }} /> */}
        </View>
    )
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f1f1f1',
    alignItems: 'center',
    padding: 32
  }
})


export default connect()(POS)