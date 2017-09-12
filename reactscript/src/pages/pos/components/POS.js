import React, { Component } from 'react'
import {
  StyleSheet,
  Text,
  View,
  DrawerLayoutAndroid,
  ToolbarAndroid,
  Dimensions,
  TextInput,
  Modal,
  TouchableNativeFeedback,
  TouchableWithoutFeedback
} from 'react-native'
import VisibleProductList from '../containers/VisibleProductList'
import Ticker from '../components/product/Ticker'
import CartContainer from '../containers/CartContainer'
import SearchContainer from '../containers/SearchContainer'
import PasswordPopup from './PasswordPopup'

export default class POS extends Component {
  constructor() {
    super()
    this.state = {
      cartOpen: false,
      searchOpen: false
    }
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
    const navigationView = (
      <View style={{ flex: 1, backgroundColor: '#fff' }}>
        <Text style={{ margin: 10, fontSize: 15, textAlign: 'left' }}>I'm in the Drawer!</Text>
        <TouchableNativeFeedback
          onPress = {() => {
            this.passwordPopup.show();
            this.drawerPane.closeDrawer();
          }}
        >
            <Text style={[{fontSize: 20, fontWeight: 'bold', margin: 10 }]}>Riwayat Transaksi</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress = {() => {
           this.props.navigation.navigate('BankSelection', {})
          }}
        >
        <Text style={[{fontSize: 20, fontWeight: 'bold', margin: 10 }]}>Bank Selection</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress = {() => {
           this.props.navigation.navigate('Payment', {})
          }}
        >
            <Text style={[{fontSize: 20, fontWeight: 'bold', margin: 10 }]}>Payment screen</Text>
        </TouchableNativeFeedback>

        <TouchableNativeFeedback
          onPress = {() => {
           this.props.navigation.navigate('PaymentProcessing', {})
          }}
        >
            <Text style={[{fontSize: 20, fontWeight: 'bold', margin: 10 }]}>Payment Processing</Text>
        </TouchableNativeFeedback>
        <TouchableNativeFeedback
          onPress = {() => {
           this.props.navigation.navigate('PaymentInvoice', {})
          }}
        >
            <Text style={[{fontSize: 20, fontWeight: 'bold', margin: 10 }]}>Payment Invoice</Text>
        </TouchableNativeFeedback>

      </View>)

    return (
      <DrawerLayoutAndroid
        drawerWidth={Dimensions.get('window').width / 2}
        drawerPosition={DrawerLayoutAndroid.positions.Left}
        ref={(drawer) => { this.drawerPane = drawer; }}
        renderNavigationView={() => navigationView}>
        <View style={styles.container}>
          <CartContainer
            visible={this.state.cartOpen}
            onBackPress={() => { this.setState({ cartOpen: false }) }} />
          <SearchContainer
            visible={this.state.searchOpen}
            onBackPress={() => { this.setState({ searchOpen: false }) }} />
          <ToolbarAndroid style={{ width: '100%', height: 60, backgroundColor: '#000' }}
            navIcon={require('./img/icon-burgermenu.png')}
            onIconClicked={this.onIconClick}
            actions={[
              {
                title: 'Credit Card',
                icon: require('./img/icon-CreditCards-Info.png'),
                show: 'always'
              },
              {
                title: 'Cart',
                icon: require('./img/icon-Cart.png'),
                show: 'always'
              },]}
            onActionSelected={this.onActionSelected}>
            <TextInput
              style={{
                borderWidth: 1,
                backgroundColor: 'white',
                borderRadius: 3,
                width: 350,
                borderColor: 'white'
              }}
              tintColor='red'
              caretHidden={false}
              placeholder='tokopedia                                                                    '
              underlineColorAndroid='transparent'
            />
          </ToolbarAndroid>
          <Ticker />
          <VisibleProductList />
          <PasswordPopup
          navigation={this.props.navigation}
           ref={(passwordPopup) => { this.passwordPopup = passwordPopup; }} />
        </View>
      </DrawerLayoutAndroid>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f1f1f1',
    alignItems: 'center',
  }
})