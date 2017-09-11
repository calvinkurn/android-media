import React, { Component } from 'react'
import { 
  Text,
  View,
  StyleSheet,
  Image,
  TouchableNativeFeedback,
  TouchableWithoutFeedback,
  TextInput
} from 'react-native'
import PopupDialog, {DialogTitle} from 'react-native-popup-dialog';

class PasswordPopup extends Component {
  constructor(props) {
    super(props)
    this.state = { errorMessage: "", password: "" }
  }

  show(onShown: ?Function) {
    this.setState({password: "", errorMessage: ""})
    this.popupDialog.show(onShown);
  }

  dismiss(onDismissed: ?Function) {
    this.popupDialog.dismiss(onDismissed);
  }

  render() {

    return (
     <PopupDialog
        dialogTitle={
          <View style={{flexDirection:'row', justifyContent: 'flex-end', padding: 10}}> 
            <TouchableWithoutFeedback onPress={() => { this.popupDialog.dismiss()}}>
            <Image source={require('../common/img/close-icon.png')} />
            </TouchableWithoutFeedback>
          </View>
        }
        dialogStyle={{borderRadius: 10}}
        width = {600}
        height= {300}
        ref={(popupDialog) => { this.popupDialog = popupDialog; }}>
      <View style={{padding: 30, paddingTop: 0, flex: 1, flexDirection: 'column', justifyContent: 'space-between', alignItems: 'center'}}>
        <Text style={{fontSize: 24, fontWeight: 'bold'}}>
          Riwayat Transaksi
        </Text>
        <View style={{padding: 10, width: '100%', marginTop: 10}}>
            <Text style={{fontSize: 16}}> Masukkan Password </Text>
            <TextInput
              style={{fontSize:20, width:"100%", marginTop:10}}
              secureTextEntry={true}
              value={this.state.password}
              underlineColorAndroid="#C6C6C6"
              placeholderTextColor = "#C6C6C6"
              onChangeText={(password) => this.setState({password})}
            />
             <Text style={{fontSize: 16, color: '#D50000'}}> {this.state.errorMessage} </Text>
        </View>
        <View style={{padding: 30, flex: 1, flexDirection: 'row', justifyContent: 'space-between'}}>
          <TouchableNativeFeedback onPress={() => { this.popupDialog.dismiss()}}>
              <View style={[styles.popupButton, {backgroundColor: "#FFFFFF", borderColor:"#F3F3F3"}]}>
                <Text style={[styles.buttonText, {color: "#888888"}]}> Batal </Text>
            </View>
          </TouchableNativeFeedback>
            <TouchableNativeFeedback onPress={() => {
                    let errorMessage = "";
                    if (this.state.password != "") {
                      this.props.navigation.navigate('TransactionHistory', {})
                    } else {
                      errorMessage = "Kata sandi salah";
                      this.setState({errorMessage});
                    }
                } }>
            <View style={[styles.popupButton, {marginLeft: 30}]}>
              <Text style={styles.buttonText}> Lanjut </Text>
            </View>
          </TouchableNativeFeedback>
        </View>
      </View>
    </PopupDialog>
    )
  }
}

const styles = StyleSheet.create({
  popupButton: {
        height: 50,
        width: "45%",
        backgroundColor: '#41B548',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        borderWidth: 3,
        borderColor: '#41B548',
      },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 20,
  }

})

export default PasswordPopup
